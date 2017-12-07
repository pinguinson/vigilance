package com.github.pinguinson.vigilance

import com.github.pinguinson.vigilance.io.IOUtils

import scala.collection.mutable.ListBuffer
import scala.reflect.internal.util.Position
import scala.tools.nsc.reporters.Reporter

/** @author Stephen Samuel */
class Feedback(consoleOutput: Boolean, reporter: Reporter) {

  val warnings = new ListBuffer[Warning]

  var levelOverridesByInspectionSimpleName: Map[String, Level] = Map.empty

  def infos = warnings(Levels.Info)
  def errors = warnings(Levels.Error)
  def warns = warnings(Levels.Warning)
  def warnings(level: Level): Seq[Warning] = warnings.filter(_.level == level)

  def warn(pos: Position,
           inspection: Inspection,
           comment: String): Unit = {

    val adjustedLevel = levelOverridesByInspectionSimpleName.getOrElse(inspection.getClass.getSimpleName, inspection.level)

    val sourceFileFull = pos.source.file.path
    val sourceFileNormalized = normalizeSourceFile(sourceFileFull)
    val warning = Warning(inspection.description, pos.line, adjustedLevel, sourceFileFull, sourceFileNormalized, comment, inspection.getClass.getCanonicalName)
    warnings.append(warning)
    if (consoleOutput) {
      val snippet = IOUtils.getSourceLine(sourceFileFull, pos.line)
      println(s"[${warning.level.toString.toLowerCase}] $sourceFileNormalized:${warning.line}: ${inspection.description}")
      println(s"          $snippet")
    }

    adjustedLevel match {
      case Levels.Error   => reporter.error(pos, inspection.description)
      case Levels.Warning => reporter.warning(pos, inspection.description)
      case Levels.Info    => reporter.info(pos, inspection.description, force = false)
    }
  }

  private def normalizeSourceFile(sourceFile: String): String = {
    val indexOf = sourceFile.indexOf("src/main/scala/")
    val packageAndFile = if (indexOf == -1) sourceFile else sourceFile.drop(indexOf).drop("src/main/scala/".length)
    packageAndFile.replace('/', '.').replace('\\', '.')
  }
}

case class Warning(text: String,
                   line: Int,
                   level: Level,
                   sourceFileFull: String,
                   sourceFileNormalized: String,
                   snippet: String,
                   inspection: String)
