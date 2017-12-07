package com.github.pinguinson.vigilance.io

import com.github.pinguinson.vigilance.{Warning, Feedback}

import scala.collection.mutable.ListBuffer
import scala.xml.Node

/** @author Eugene Sypachev (Axblade) */
object ScalastyleReportWriter {

  private val checkstyleVersion = "5.0"
  private val vigilance = "vigilance"

  def toXML(feedback: Feedback): Node = {
    <checkstyle version={checkstyleVersion} generatedBy={vigilance}>
      {feedback.warnings.groupBy(_.sourceFileFull).map(fileToXml)}
    </checkstyle>
  }

  private def fileToXml(fileWarningMapEntry: (String, ListBuffer[Warning])) = {
    val (file, warnings) = fileWarningMapEntry
    <file name={file}>
      {warnings.map(warningToXml)}
    </file>
  }

  private def warningToXml(warning: Warning) = {
    <error line={warning.line.toString} message={warning.text} severity={warning.level.toString} source={warning.inspection} snippet={warning.snippet}></error>
  }

}
