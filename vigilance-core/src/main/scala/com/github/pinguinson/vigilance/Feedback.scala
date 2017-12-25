package com.github.pinguinson.vigilance

import com.github.pinguinson.vigilance.io.IOUtils

import scala.collection.mutable.ListBuffer
import scala.reflect.internal.util.Position
import scala.tools.nsc.reporters.Reporter

/** @author Stephen Samuel */
class Feedback(consoleOutput: Boolean, reporter: Reporter) {

  val reports = new ListBuffer[Report]

  var levelOverridesByInspectionSimpleName: Map[String, Level] = Map.empty

  def infos = reports(Levels.Info)
  def errors = reports(Levels.Error)
  def warns = reports(Levels.Warning)
  def styles = reports(Levels.Style)

  def reports(level: Level): Seq[Report] = reports.filter(_.level == level)

  def warn(pos: Position, inspection: Inspection, comment: String): Unit = {

    val adjustedLevel = levelOverridesByInspectionSimpleName.getOrElse(inspection.getClass.getSimpleName, inspection.level)

    val sourceFileFull = pos.source.file.path
    val sourceFileNormalized = normalizeSourceFile(sourceFileFull)
    val report = Report(inspection.description, pos.line, adjustedLevel, sourceFileFull, sourceFileNormalized, comment, inspection.getClass.getCanonicalName)
    reports.append(report)
    if (consoleOutput) {
      //TODO: does not seem to work
      val snippet = IOUtils.getSourceLines(sourceFileFull, pos.start, pos.end)
      println(s"[${report.level.toString.toLowerCase}] $sourceFileNormalized:${report.line}: $comment")
      println(s"          $snippet")
    }

    adjustedLevel match {
      case Levels.Error   => reporter.error(pos, inspection.description)
      case Levels.Warning => reporter.warning(pos, inspection.description)
      case _              => reporter.info(pos, inspection.description, force = false)
    }
  }

  private def normalizeSourceFile(sourceFile: String): String = {
    val indexOf = sourceFile.indexOf("src/main/scala/")
    val packageAndFile = if (indexOf == -1) sourceFile else sourceFile.drop(indexOf).drop("src/main/scala/".length)
    packageAndFile.replace('/', '.').replace('\\', '.')
  }
}

case class Report(title: String,
                  line: Int,
                  level: Level,
                  sourceFileFull: String,
                  sourceFileNormalized: String,
                  comment: String,
                  inspection: String)
