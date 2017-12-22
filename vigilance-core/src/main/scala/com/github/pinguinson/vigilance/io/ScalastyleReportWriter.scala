package com.github.pinguinson.vigilance.io

import com.github.pinguinson.vigilance.{Report, Feedback}

import scala.collection.mutable.ListBuffer
import scala.xml.Node

/** @author Eugene Sypachev (Axblade) */
object ScalastyleReportWriter {

  private val checkstyleVersion = "5.0"
  private val vigilance = "vigilance"

  def toXML(feedback: Feedback): Node = {
    <checkstyle version={checkstyleVersion} generatedBy={vigilance}>
      {feedback.reports.groupBy(_.sourceFileFull).map(fileToXml)}
    </checkstyle>
  }

  private def fileToXml(fileWarningMapEntry: (String, ListBuffer[Report])) = {
    val (file, reports) = fileWarningMapEntry
    <file name={file}>
      {reports.map(reportToXml)}
    </file>
  }

  private def reportToXml(report: Report) = {
    <error line={report.line.toString} message={report.title} severity={report.level.toString} source={report.inspection} snippet={report.comment}></error>
  }

}
