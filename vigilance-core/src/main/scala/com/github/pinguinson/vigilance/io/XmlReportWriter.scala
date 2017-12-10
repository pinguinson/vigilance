package com.github.pinguinson.vigilance.io

import com.github.pinguinson.vigilance.{Feedback, Report}

import scala.xml.Node

/** @author Stephen Samuel */
object XmlReportWriter {

  def toXML(feedback: Feedback): Node = {
    <vigilance count={feedback.reports.size.toString} warns={feedback.warns.size.toString} errors={feedback.errors.size.toString} infos={feedback.infos.size.toString}>
      {feedback.reports.map(reportToXml)}
    </vigilance>
  }

  private def reportToXml(report: Report) = {
      <warning line={report.line.toString} text={report.text} snippet={report.snippet} level={report.level.toString} file={report.sourceFileNormalized} inspection={report.inspection}/>
  }
}
