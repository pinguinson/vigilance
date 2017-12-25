package com.github.pinguinson.vigilance.io

import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}
import java.nio.charset.StandardCharsets

import com.github.pinguinson.vigilance.Feedback

import scala.io.Source

/**
 * @author Stephen Samuel
 * @author Eugene Sypachev (Axblade)
 */
object IOUtils {

  private val XmlFile           = "vigilance.xml"
  private val ScalastyleXmlFile = "vigilance-scalastyle.xml"
  private val HtmlFile          = "vigilance.html"

  def serialize(file: File, str: String): Unit = {
    val out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8.name))
    out.write(str)
    out.close()
  }

  def writeHTMLReport(targetDir: File, reporter: Feedback): File = {
    val html = HtmlReportWriter.generate(reporter).toString
    writeFile(targetDir, reporter, html, HtmlFile)
  }

  def writeXMLReport(targetDir: File, reporter: Feedback): File = {
    val html = XmlReportWriter.toXML(reporter).toString
    writeFile(targetDir, reporter, html, XmlFile)
  }

  def writeScalastyleReport(targetDir: File, reporter: Feedback): File = {
    val html = ScalastyleReportWriter.toXML(reporter).toString
    writeFile(targetDir, reporter, html, ScalastyleXmlFile)
  }

  def getSourceLine(filePath: String, lineNumber: Int): String = {
    Source.fromFile(filePath).getLines.toStream.drop(lineNumber - 1).headOption.getOrElse("").trim
  }

  def getSourceLines(filePath: String, startLine: Int, endLine: Int): String = {
    Source.fromFile(filePath).getLines.toStream.slice(startLine - 1, endLine).mkString("\n").trim
  }


  private def writeFile(targetDir: File, reporter: Feedback, data: String, fileName: String) = {
    targetDir.mkdirs()
    val file = new File(targetDir.getAbsolutePath + "/" + fileName)
    serialize(file, data)
    file
  }

}
