package com.sksamuel.scapegoat.sbt

import sbt._
import sbt.complete.DefaultParsers._
import sbt.Keys._

object ScapegoatSbtPlugin extends AutoPlugin {

  val GroupId = "com.github.pinguinson"
  val ArtifactId = "scalac-scapegoat-plugin"

  object autoImport {
    val Scapegoat = config("scapegoat") extend Compile

    lazy val scapegoat = taskKey[Unit]("Run scapegoat quality checks")
    lazy val scapegoatCleanTask = taskKey[Unit]("Conditionally clean the scapegoat output directories")
    lazy val scapegoatClean = taskKey[Unit]("Clean the scapegoat output directories")
    lazy val scapegoatVersion = settingKey[String]("The version of the scala plugin to use")
    lazy val scapegoatDisabledInspections = settingKey[Seq[String]]("Inspections that are disabled globally")
    lazy val scapegoatEnabledInspections = settingKey[Seq[String]]("Inspections that are explicitly enabled")
    lazy val scapegoatRunAlways = settingKey[Boolean]("Force inspections to run even on files that haven't changed")
    lazy val scapegoatIgnoredFiles = settingKey[Seq[String]]("File patterns to ignore")
    lazy val scapegoatDiffBranch = settingKey[Option[String]]("Only check files in diff against this branch")
    lazy val scapegoatMaxErrors = settingKey[Int]("Maximum number of errors before the build will fail")
    lazy val scapegoatMaxWarnings = settingKey[Int]("Maximum number of warnings before the build will fail")
    lazy val scapegoatMaxInfos = settingKey[Int]("Maximum number of infos before the build will fail")
    lazy val scapegoatConsoleOutput = settingKey[Boolean]("Output results of scan to the console during compilation")
    lazy val scapegoatOutputPath = settingKey[String]("Directory where reports will be written")
    lazy val scapegoatVerbose = settingKey[Boolean]("Verbose mode for inspections")
    lazy val scapegoatReports = settingKey[Seq[String]]("The report styles to generate")
    lazy val scapegoatDiff = inputKey[Unit]("only process files in diff against this branch")
  }

  import autoImport._

  def doScapegoatClean(force: Boolean, classesDir: File, log: Logger): Unit = {
    if (force) {
      log.info(s"[scapegoat] Removing scapegoat class directory: $classesDir")
      IO.delete(Seq(classesDir))
    }
  }

  override def trigger = allRequirements
  override def projectSettings = {
    inConfig(Scapegoat) {
      Defaults.compileSettings ++
        Seq(
          sources := (sources in Compile).value,
          managedClasspath := (managedClasspath in Compile).value,
          unmanagedClasspath := (unmanagedClasspath in Compile).value,
          scalacOptions := {
            // find all deps for the compile scope
            val scapegoatDependencies = (update in Scapegoat).value matching configurationFilter(Compile.name)
            // ensure we have the scapegoat dependency on the classpath and if so add it as a scalac plugin
            scapegoatDependencies.find(_.getAbsolutePath.contains(ArtifactId)) match {
              case None => throw new Exception(s"Fatal: $ArtifactId not in libraryDependencies ($scapegoatDependencies)")
              case Some(classpath) =>

                val verbose = scapegoatVerbose.value
                val path = scapegoatOutputPath.value
                val reports = scapegoatReports.value
                val logger = streams.value

                if (verbose)
                  logger.log.info(s"[scapegoat] setting output dir to [$path]")

                val disabled = scapegoatDisabledInspections.value.filterNot(_.trim.isEmpty)
                if (disabled.nonEmpty && verbose)
                  logger.log.info("[scapegoat] disabled inspections: " + disabled.mkString(","))

                val enabled = scapegoatEnabledInspections.value.filterNot(_.trim.isEmpty)
                if (enabled.nonEmpty && verbose)
                  logger.log.info("[scapegoat] enabled inspections: " + enabled.mkString(","))

                val ignoredFilePatterns = scapegoatIgnoredFiles.value.filterNot(_.trim.isEmpty)
                if (ignoredFilePatterns.nonEmpty && verbose)
                  logger.log.info("[scapegoat] ignored file patterns: " + ignoredFilePatterns.mkString(","))

                val diffBranch = scapegoatDiffBranch.value.filterNot(_.trim.isEmpty)
                diffBranch.foreach { branch =>
                  if (verbose) logger.log.info(s"[scapegoat] diff against: $branch")
                }

                (scalacOptions in Compile).value ++ Seq(
                  Some("-Xplugin:" + classpath.getAbsolutePath),
                  Some("-P:scapegoat:verbose:" + scapegoatVerbose.value),
                  Some("-P:scapegoat:consoleOutput:" + scapegoatConsoleOutput.value),
                  Some("-P:scapegoat:dataDir:" + path),
                  if (disabled.isEmpty) None else Some("-P:scapegoat:disabledInspections:" + disabled.mkString(":")),
                  if (enabled.isEmpty) None else Some("-P:scapegoat:enabledInspections:" + enabled.mkString(":")),
                  if (ignoredFilePatterns.isEmpty) None else Some("-P:scapegoat:ignoredFiles:" + ignoredFilePatterns.mkString(":")),
                  if (reports.isEmpty) None else Some("-P:scapegoat:reports:" + reports.mkString(":")),
                  diffBranch.map("-P:scapegoat:diff:" + _)
                ).flatten
            }
          }
        )
    } ++ Seq(
      (compile in Scapegoat) := ((compile in Scapegoat) dependsOn scapegoatClean).value,
      scapegoat := (compile in Scapegoat).value,
      scapegoatDiff := {
        spaceDelimited("<arg>").parsed.headOption match {
          case Some(branch) =>
            val extracted = Project.extract(state.value)
            val newState = extracted.append(Seq(scapegoatDiffBranch := Some(branch)), state.value)
            Project.extract(newState).runTask(scapegoat, newState)
          case None =>
            throw new IllegalArgumentException("Usage: scapegoatDiff <branchName>")
        }
      },
      scapegoatCleanTask := doScapegoatClean(scapegoatRunAlways.value, (classDirectory in Scapegoat).value, streams.value.log),
      scapegoatClean := doScapegoatClean(true, (classDirectory in Scapegoat).value, streams.value.log),
      scapegoatRunAlways := true,
      scapegoatVersion := "1.0.0",
      scapegoatConsoleOutput := true,
      scapegoatVerbose := true,
      scapegoatMaxInfos := -1,
      scapegoatMaxWarnings := -1,
      scapegoatMaxErrors := -1,
      scapegoatDisabledInspections := Nil,
      scapegoatEnabledInspections := Nil,
      scapegoatIgnoredFiles := Nil,
      scapegoatDiffBranch := None,
      scapegoatOutputPath := (crossTarget in Compile).value.getAbsolutePath + "/scapegoat-report",
      scapegoatReports := Seq("all"),
      libraryDependencies ++= Seq(
        GroupId % (ArtifactId + "_" + scalaBinaryVersion.value) % (scapegoatVersion in Scapegoat).value % Compile.name
      )
    )
  }
}
