package com.github.pinguinson.vigilance.io

import com.github.pinguinson.vigilance.{Feedback, Level, Levels}

import scala.xml.Unparsed

/** @author Stephen Samuel */
object HtmlReportWriter {

  def generate(reporter: Feedback) =
    <html>
      {header}
      {body(reporter)}
    </html>

  private def body(reporter: Feedback) =
    <body>
      <h1>Vigilance Inspections</h1>
      <h3>
        Errors
        {reporter.reports(Levels.Error).size.toString}
        Warnings
        {reporter.reports(Levels.Warning).size.toString}
        Infos
        {reporter.reports(Levels.Info).size.toString}
        Styles
        {reporter.reports(Levels.Style).size.toString}
      </h3>
      {reportsToXml(reporter)}
    </body>

  private def reportsToXml(reporter: Feedback) = {
    reporter.reports
      .sortBy(_.level.importance)
      .map { warning =>
        <div class="warning">
          <div class="source">
            {warning.sourceFileNormalized + ":" + warning.line}
          </div>
          <div class="title">
            {levelSpan(warning.level)}
            &nbsp;{warning.text}&nbsp;
            <span class="inspection">{warning.inspection}</span>
          </div>
          <div class="snippet">{IOUtils.getSourceLine(warning.sourceFileFull, warning.line)}</div>
          <div class="snippet">{warning.snippet}</div>
        </div>
      }
  }

  private def levelSpan(level: Level) = level match {
    case Levels.Style   => <span class="label label-success">Style</span>
    case Levels.Info    => <span class="label label-info">Info</span>
    case Levels.Warning => <span class="label label-warning">Warning</span>
    case Levels.Error   => <span class="label label-danger">Error</span>
  }

  private val css =
    """
      |body {
      |  font-family: 'Ubuntu', sans-serif;
      |  padding: 0 15px;
      |}
      |
      |h1 {
      |	 color: #515151;
      |	 font-weight: 700;
      |}
      |
      |h3 {
      |	 color: #8a8a8a;
      |	 font-weight: 400;
      |}
      |
      |.warning {
      |	 background :#F1F3F2;
      |	 border-bottom-left-radius: 6px;
      |  border-bottom-right-radius: 6px;
      |	 margin-bottom: 3px;
      |	 padding: 12px;
      |}
      |
      |.title {
      |  color: #616161;
      |	 font-size: 16px;
      |}
      |
      |.source {
      |  float: right;
      |	 font-style: italic;
      |  color: #868686;
      |}
      |
      |.snippet {
      |  padding-top: 8px;
      |  color: #0C0C0C;
      |  font-family: 'Ubuntu Mono', sans-serif;
      |  font-weight: 300;
      |  font-size: 12px;
      |}
      |
      |.inspection {
      |  padding-left: 10px;
      |  font-style:italic;
      |  color: #969696;
      |  font-size: 12px;
      |}
    """.stripMargin

  private val fontSizes = Seq(
    "300",
    "400",
    "500",
    "600",
    "700",
    "300italic",
    "400italic",
    "500italic",
    "600italic",
    "700italic"
  ).mkString(",")

  private val header =
    <head>
      <title>Vigilance Inspection Reporter</title>
      {Unparsed("""<link href='http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css' rel='stylesheet'>""")}
      {Unparsed(s"""<link href='http://fonts.googleapis.com/css?family=Ubuntu:$fontSizes' rel='stylesheet' type='text/css'>""")}
      {Unparsed(s"""<link href='http://fonts.googleapis.com/css?family=Open+Sans:$fontSizes' rel='stylesheet' type='text/css'>""")}
      <style>{css}</style>
    </head>
}
