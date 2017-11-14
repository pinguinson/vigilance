package com.github.pinguinson.vigilance.io

import java.io.{ BufferedWriter, File, FileWriter }

import com.github.pinguinson.vigilance.Feedback

/**
 * @author Stephen Samuel
 * @author Eugene Sypachev (Axblade)
 */
object IOUtils {

  private val XmlFile = "vigilance.xml"
  private val ScalastyleXmlFile = "vigilance-scalastyle.xml"
  private val HtmlFile = "vigilance.html"

  def serialize(file: File, str: String) = {
    val out = new BufferedWriter(new FileWriter(file))
    out.write(str)
    out.close()
  }

  def writeHTMLReport(targetDir: File, reporter: Feedback): File = {
    val html = HtmlReportWriter.generate(reporter).toString()
    writeFile(targetDir, reporter, html, HtmlFile)
  }

  def writeXMLReport(targetDir: File, reporter: Feedback): File = {
    val html = XmlReportWriter.toXML(reporter).toString()
    writeFile(targetDir, reporter, html, XmlFile)
  }

  def writeScalastyleReport(targetDir: File, reporter: Feedback): File = {
    val html = ScalastyleReportWriter.toXML(reporter).toString()
    writeFile(targetDir, reporter, html, ScalastyleXmlFile)
  }

  private def writeFile(targetDir: File, reporter: Feedback, data: String, fileName: String) = {
    targetDir.mkdirs()
    val file = new File(targetDir.getAbsolutePath + "/" + fileName)
    serialize(file, data)
    file
  }

}
