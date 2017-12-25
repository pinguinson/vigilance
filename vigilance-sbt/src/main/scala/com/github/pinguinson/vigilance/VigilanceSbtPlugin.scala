package com.github.pinguinson.vigilance

import sbt._
import sbt.complete.DefaultParsers._
import sbt.Keys._

object VigilanceSbtPlugin extends AutoPlugin {

  val GroupId = "com.github.pinguinson"
  val ArtifactId = "scalac-vigilance-plugin"

  object autoImport {
    val Vigilance = config("vigilance") extend Compile

    lazy val vigilance = taskKey[Unit]("Run vigilance quality checks")
    lazy val vigilanceCleanTask = taskKey[Unit]("Conditionally clean the vigilance output directories")
    lazy val vigilanceClean = taskKey[Unit]("Clean the vigilance output directories")
    lazy val vigilanceDisabledInspections = settingKey[Seq[String]]("Inspections that are disabled globally")
    lazy val vigilanceEnabledInspections = settingKey[Seq[String]]("Inspections that are explicitly enabled")
    lazy val vigilanceRunAlways = settingKey[Boolean]("Force inspections to run even on files that haven't changed")
    lazy val vigilanceIgnoredFiles = settingKey[Seq[String]]("File patterns to ignore")
    lazy val vigilanceDiffBranch = settingKey[Option[String]]("Only check files in diff against this branch")
    lazy val vigilanceMaxErrors = settingKey[Int]("Maximum number of errors before the build will fail")
    lazy val vigilanceMaxWarnings = settingKey[Int]("Maximum number of warnings before the build will fail")
    lazy val vigilanceMaxInfos = settingKey[Int]("Maximum number of infos before the build will fail")
    lazy val vigilanceConsoleOutput = settingKey[Boolean]("Output results of scan to the console during compilation")
    lazy val vigilanceOutputPath = settingKey[String]("Directory where reports will be written")
    lazy val vigilanceVerbose = settingKey[Boolean]("Verbose mode for inspections")
    lazy val vigilanceReports = settingKey[Seq[String]]("The report styles to generate")
    lazy val vigilanceDiff = inputKey[Unit]("only process files in diff against this branch")
  }

  import autoImport._

  def doVigilanceClean(force: Boolean, classesDir: File, log: Logger): Unit = {
    if (force) {
      log.info(s"[vigilance] Removing vigilance class directory: $classesDir")
      IO.delete(Seq(classesDir))
    }
  }

  override def trigger = allRequirements
  override def projectSettings = {
    inConfig(Vigilance) {
      Defaults.compileSettings ++
        Seq(
          sources := (sources in Compile).value,
          managedClasspath := (managedClasspath in Compile).value,
          unmanagedClasspath := (unmanagedClasspath in Compile).value,
          scalacOptions := {
            // find all deps for the compile scope
            val vigilanceDependencies = (update in Vigilance).value matching configurationFilter(Compile.name)
            // ensure we have the vigilance dependency on the classpath and if so add it as a scalac plugin
            vigilanceDependencies.find(_.getAbsolutePath.endsWith(s"${ArtifactId}_${scalaBinaryVersion.value}.jar")) match {
              case None => throw new Exception(s"Fatal: ${ArtifactId}_${scalaBinaryVersion.value} not in libraryDependencies ($vigilanceDependencies)")
              case Some(classpath) =>

                val verbose = vigilanceVerbose.value
                val path = vigilanceOutputPath.value
                val reports = vigilanceReports.value
                val logger = streams.value

                if (verbose)
                  logger.log.info(s"[vigilance] setting output dir to [$path]")

                val disabled = vigilanceDisabledInspections.value.filterNot(_.trim.isEmpty)
                if (disabled.nonEmpty && verbose)
                  logger.log.info("[vigilance] disabled inspections: " + disabled.mkString(","))

                val enabled = vigilanceEnabledInspections.value.filterNot(_.trim.isEmpty)
                if (enabled.nonEmpty && verbose)
                  logger.log.info("[vigilance] enabled inspections: " + enabled.mkString(","))

                val ignoredFilePatterns = vigilanceIgnoredFiles.value.filterNot(_.trim.isEmpty)
                if (ignoredFilePatterns.nonEmpty && verbose)
                  logger.log.info("[vigilance] ignored file patterns: " + ignoredFilePatterns.mkString(","))

                val diffBranch = vigilanceDiffBranch.value.filterNot(_.trim.isEmpty)
                diffBranch.foreach { branch =>
                  if (verbose) logger.log.info(s"[vigilance] diff against: $branch")
                }

                (scalacOptions in Compile).value ++ Seq(
                  Some("-Xplugin:" + classpath.getAbsolutePath),
                  Some("-P:vigilance:verbose:" + vigilanceVerbose.value),
                  Some("-P:vigilance:consoleOutput:" + vigilanceConsoleOutput.value),
                  Some("-P:vigilance:dataDir:" + path),
                  if (disabled.isEmpty) None else Some("-P:vigilance:disabledInspections:" + disabled.mkString(":")),
                  if (enabled.isEmpty) None else Some("-P:vigilance:enabledInspections:" + enabled.mkString(":")),
                  if (ignoredFilePatterns.isEmpty) None else Some("-P:vigilance:ignoredFiles:" + ignoredFilePatterns.mkString(":")),
                  if (reports.isEmpty) None else Some("-P:vigilance:reports:" + reports.mkString(":")),
                  diffBranch.map("-P:vigilance:diff:" + _)
                ).flatten
            }
          }
        )
    } ++ Seq(
      (compile in Vigilance) := ((compile in Vigilance) dependsOn vigilanceClean).value,
      vigilance := (compile in Vigilance).value,
      vigilanceDiff := {
        spaceDelimited("<arg>").parsed.headOption match {
          case Some(branch) =>
            val extracted = Project.extract(state.value)
            val newState = extracted.append(Seq(vigilanceDiffBranch := Some(branch)), state.value)
            Project.extract(newState).runTask(vigilance, newState)
          case None =>
            throw new IllegalArgumentException("Usage: vigilanceDiff <branchName>")
        }
      },
      vigilanceCleanTask := doVigilanceClean(vigilanceRunAlways.value, (classDirectory in Vigilance).value, streams.value.log),
      vigilanceClean := doVigilanceClean(true, (classDirectory in Vigilance).value, streams.value.log),
      vigilanceRunAlways := true,
      vigilanceConsoleOutput := true,
      vigilanceVerbose := false,
      vigilanceMaxInfos := -1,
      vigilanceMaxWarnings := -1,
      vigilanceMaxErrors := -1,
      vigilanceDisabledInspections := Nil,
      vigilanceEnabledInspections := Nil,
      vigilanceIgnoredFiles := Nil,
      vigilanceDiffBranch := None,
      vigilanceOutputPath := (crossTarget in Compile).value.getAbsolutePath + "/vigilance-report",
      vigilanceReports := Seq("all"),
      libraryDependencies += GroupId %% ArtifactId % "0.0.7" % Compile
    )
  }
}
