import $ivy.`com.github.pathikrit::better-files:3.4.0`

import ammonite.ops._
import better.files._

@main
def main(versionFileName: String = "version.sbt", path: Path = pwd) = {
  val versionFile = File(s"$path/$versionFileName")
  val contents = versionFile.contentAsString
  val currentVersionOpt = 
    contents
      .split(" := ")
      .toSeq
      .map(_.filter(_ >= ' '))
      .lastOption

  val versionRegex = """([\d\."]+)""".r
  val currentVersion = currentVersionOpt match {
    case Some(versionRegex(version)) => version
    case Some(version) => throw new RuntimeException(s"Looks like a snapshot version: $version")
    case _ =>             throw new RuntimeException(s"Failed to parse $versionFileName")
  }

  val readmeFile = File(s"$path/README.md")
  val pluginRegex = """addSbtPlugin\(("com.github.pinguinson" % "sbt-vigilance") % ("[\w\.\-]+")\)""".r

  val updatedReadme = readmeFile
    .contentAsString
    .split("\n")
    .toSeq
    .map {
      case pluginRegex(prefix, `currentVersion`) =>
        throw new RuntimeException(s"Current version is already set")
      case pluginRegex(prefix, version) =>
        println(s"Replacing: $version -> $currentVersion")
        s"addSbtPlugin($prefix % $currentVersion)"
      case row => row
    }
    .mkString("\n")

  readmeFile.overwrite(updatedReadme)
  println("Updated README.md")
}
