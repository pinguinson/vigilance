package com.github.pinguinson.vigilance

import java.io.{File, FileNotFoundException}
import java.nio.charset.StandardCharsets

import scala.tools.nsc.reporters.ConsoleReporter

/**
  * @author Stephen Samuel
  * @author Eugene Sypachev (Axblade)
  */
trait PluginRunner {

  def inspections: Seq[Inspection]

  val scalaVersion = scala.util.Properties.versionNumberString
  val shortScalaVersion = scalaVersion.split('.').init.mkString(".")

  val classPath = getScalaJars.map(_.getAbsolutePath) :+ sbtCompileDir.getAbsolutePath

  val settings = {
    val s = new scala.tools.nsc.Settings
    Option(System.getProperty("printphases")).foreach { _ =>
      s.Xprint.value = List("all")
      s.Yrangepos.value = true
      s.Yposdebug.value = true
    }
    s.stopAfter.value = List("refchecks") // no need to go all the way to generating classfiles
    s.classpath.value = classPath.mkString(File.pathSeparator)
    s.feature.value = true
    s
  }

  val reporter = new ConsoleReporter(settings)
  lazy val compiler = new VigilanceCompiler(settings, inspections, reporter)

  def compileCodeSnippet(code: String): VigilanceCompiler = {
    compileSourceFiles(writeCodeSnippetToTempFile(code))
  }

  def addToClassPath(groupId: String, artifactId: String, version: String): Unit = {
    val jarPath = findIvyJar(groupId, artifactId, version).getAbsolutePath
    settings.classpath.value = settings.classpath.value + ":" + jarPath
  }

  private def writeCodeSnippetToTempFile(code: String): File = {
    val file = File.createTempFile("vigilanceSnippet", ".scala")
    org.apache.commons.io.FileUtils.write(file, code, StandardCharsets.UTF_8)
    file.deleteOnExit()
    file
  }

  private def compileSourceFiles(files: File*): VigilanceCompiler = {
    reporter.flush()
    val command = new scala.tools.nsc.CompilerCommand(files.map(_.getAbsolutePath).toList, settings)
    new compiler.Run().compile(command.files)
    compiler
  }

  private def getScalaJars: List[File] = {
    val scalaJars = List("scala-compiler", "scala-library", "scala-reflect")
    scalaJars.map(findScalaJar)
  }

  private def findScalaJar(artifactId: String): File = findIvyJar("org.scala-lang", artifactId, scalaVersion)

  private def findIvyJar(groupId: String, artifactId: String, version: String): File = {
    val userHome = System.getProperty("user.home")
    val sbtHome = userHome + "/.ivy2"
    val artifactFolder = s"$sbtHome/cache/$groupId/$artifactId/"
    val possibleFolders = Seq("jars", "bundles")
    val artifactFileName = s"$artifactId-$version.jar"
    val possibleLocations = possibleFolders.map { subFolder =>
      val fullPath = s"$artifactFolder/$subFolder/$artifactFileName"
      new File(fullPath)
    }
    possibleLocations.find(_.exists).getOrElse {
      val paths = possibleLocations.map(_.getAbsolutePath).mkString("\n")
      val errorMessage =
        s"""Could not locate jar. Tried:
           |$paths""".stripMargin
      throw new FileNotFoundException(errorMessage)
    }
  }

  private def sbtCompileDir: File = {
    val dir = new File("./vigilance-core/target/scala-" + shortScalaVersion + "/classes")
    if (dir.exists) dir
    else throw new FileNotFoundException(s"Could not locate SBT compile directory for plugin files [$dir]")
  }
}

class VigilanceCompiler(settings: scala.tools.nsc.Settings,
                        inspections: Seq[Inspection],
                        reporter: ConsoleReporter)
  extends scala.tools.nsc.Global(settings, reporter) {

  val vigilance = new VigilanceComponent(this, inspections)
  vigilance.disableHTML = true
  vigilance.disableXML = true
  vigilance.disableScalastyleXML = true
  vigilance.verbose = false
  vigilance.summary = false

  override def computeInternalPhases() {
    super.computeInternalPhases()
    phasesSet.add(vigilance)
    phasesDescMap.put(vigilance, "vigilance")
  }
}
