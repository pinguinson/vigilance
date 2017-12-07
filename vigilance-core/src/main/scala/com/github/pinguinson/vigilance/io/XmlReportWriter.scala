package com.github.pinguinson.vigilance.io

import com.github.pinguinson.vigilance.{Feedback, Warning}

import scala.xml.Node

/** @author Stephen Samuel */
object XmlReportWriter {

  def toXML(feedback: Feedback): Node = {
    <vigilance count={feedback.warnings.size.toString} warns={feedback.warns.size.toString} errors={feedback.errors.size.toString} infos={feedback.infos.size.toString}>
      {feedback.warnings.map(warning2xml)}
    </vigilance>
  }

  private def warning2xml(warning: Warning) = {
      <warning line={warning.line.toString} text={warning.text} snippet={warning.snippet} level={warning.level.toString} file={warning.sourceFileNormalized} inspection={warning.inspection}/>
  }
}
