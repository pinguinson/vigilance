package com.github.pinguinson.vigilance.io

import com.github.pinguinson.vigilance.{Feedback, Level, Levels}

import scala.xml.{NodeSeq, Unparsed}

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
        {reporter.warnings(Levels.Error).size.toString}
        Warnings
        {reporter.warnings(Levels.Warning).size.toString}
        Infos
        {reporter.warnings(Levels.Info).size.toString}
      </h3>
      {warnings(reporter)}
    </body>

  private def warnings(reporter: Feedback) = reporter.warnings.map { warning =>
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
      {warning.snippet.map(snippet => <div class="snippet">{snippet}</div>).getOrElse(NodeSeq.Empty)}
    </div>
  }
  private def levelSpan(level: Level) = level match {
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
      |	color: #515151;
      |	font-weight: 700;
      |}
      |
      |h3 {
      |	color: #8a8a8a;
      |	font-weight: 400;
      |}
      |
      |.warning {
      |	  background :#F1F3F2;
      |	  border-bottom-left-radius: 6px;
      |  border-bottom-right-radius: 6px;
      |	  margin-bottom: 3px;
      |	  padding: 12px;
      |}
      |
      |.title {
      |	color: #616161;
      |	font-size: 16px;
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

  private val header =
    <head>
      <title>Vigilance Inspection Reporter</title>
      {Unparsed("""<link href='http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css' rel='stylesheet'>""")}
      {Unparsed("""<link href='http://fonts.googleapis.com/css?family=Ubuntu:300,400,500,700,300italic,400italic,500italic,700italic' rel='stylesheet' type='text/css'>""")}
      {Unparsed("""<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>""")}
      <style>{css}</style>
    </head>
}
