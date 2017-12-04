package com.github.pinguinson.vigilance

import java.io.File
import java.util.concurrent.atomic.AtomicInteger

import com.github.pinguinson.vigilance.io.{GitUtils, IOUtils}

import scala.tools.nsc._
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc.transform.{Transform, TypingTransformers}

class VigilancePlugin(val global: Global) extends Plugin {

  override val name: String = "vigilance"
  override val description: String = "vigilance compiler plugin"
  val component = new VigilanceComponent(global, VigilanceConfig.inspections)
  override val components: List[PluginComponent] = List(component)

  override def init(options: List[String], error: String => Unit): Boolean = {
    options.find(_.startsWith("disabledInspections:")) match {
      case Some(option) => component.disabled = option.drop("disabledInspections:".length).split(':').toList
      case _            =>
    }
    options.find(_.startsWith("consoleOutput:")) match {
      case Some(option) => component.consoleOutput = option.drop("consoleOutput:".length).toBoolean
      case _            =>
    }
    options.find(_.startsWith("ignoredFiles:")) match {
      case Some(option) => component.ignoredFiles = option.drop("ignoredFiles:".length).split(':').toList
      case _            =>
    }
    for (verbose <- options.find(_.startsWith("verbose:"))) {
      component.verbose = verbose.drop("verbose:".length).toBoolean
    }
    options.find(_.startsWith("customInspectors:")) match {
      case Some(option) => component.customInspections =
        option.drop("customInspectors:".length)
          .split(':')
          .toSeq
          .map(inspection => Class.forName(inspection).newInstance.asInstanceOf[Inspection])
      case _ =>
    }
    options.find(_.startsWith("reports:")) match {
      case Some(option) =>
        option.drop("reports:".length)
          .split(':')
          .toSeq
          .foreach {
            case "xml"        => component.disableXML = false
            case "html"       => component.disableHTML = false
            case "scalastyle" => component.disableScalastyleXML = false
            case "all" =>
              component.disableXML = false
              component.disableHTML = false
              component.disableScalastyleXML = false
            case "none" =>
              component.disableXML = true
              component.disableHTML = true
              component.disableScalastyleXML = true
            case _ =>
          }
      case None =>
        component.disableXML = false
        component.disableHTML = false
        component.disableScalastyleXML = false
    }
    options.find(_.startsWith("overrideLevels:")) foreach { option =>
      val nameLevels = option.drop("overrideLevels:".length).split(":")
      component.feedback.levelOverridesByInspectionSimpleName = nameLevels.map { nameLevel =>
        nameLevel.split("=") match {
          case Array(inspection, level) => inspection -> Levels.fromName(level)
          case _ =>
            throw new IllegalArgumentException(
              s"Malformed argument to 'overrideLevels': '$nameLevel'. " +
                "Expecting 'name=level' where 'name' is the simple name of " +
                "an inspection and 'level' is the simple name of a " +
                "com.github.pinguinson.vigilance.Level constant, e.g. 'Warning'.")
        }
      }.toMap
    }
    options.find(_.startsWith("diff:")) match {
      case Some(option) =>
        val branch = option.drop(":diff".length)
        component.diffBranch = Some(branch)
        component.diffFiles = GitUtils.diff(branch).toList
      case _            =>
    }
    options.find(_.startsWith("dataDir:")) match {
      case Some(option) =>
        component.dataDir = new File(option.drop("dataDir:".length))
        true
      case None =>
        error("-P:vigilance:dataDir not specified")
        false
    }

  }

  override val optionsHelp: Option[String] = Some(Seq(
    "-P:vigilance:dataDir:<pathToDataDir>                 where the report should be written",
    "-P:vigilance:disabled:<listOfInspections>            colon separated list of disabled inspections",
    "-P:vigilance:customInspectors:<listOfInspections>    colon separated list of custom inspections",
    "-P:vigilance:ignoredFiles:<patterns>                 colon separated list of regexes to match ",
    "                                                     files to ignore.",
    "-P:vigilance:verbose:<boolean>                       enable/disable verbose console messages",
    "-P:vigilance:consoleOutput:<boolean>                 enable/disable console report output",
    "-P:vigilance:reports:<reports>                       colon separated list of reports to generate.",
    "                                                     Valid options are `xml', `html', `scalastyle',",
    "                                                     or `all'.",
    "-P:vigilance:overrideLevels:<levels>                 override the built in warning levels, e.g. to",
    "                                                     downgrade a Error to a Warning.",
    "                                                     <levels> should be a colon separated list of name=level",
    "                                                     settings, where 'name' is the simple name of an inspection",
    "                                                     and 'level' is the simple name of a",
    "                                                     com.github.pinguinson.vigilance.Level constant, e.g. 'Warning'.",
    "-P:vigilance:diff:<branch>                           only check files in diff against this branch")
    .mkString("\n"))
}

class VigilanceComponent(val global: Global, inspections: Seq[Inspection])
    extends PluginComponent with TypingTransformers with Transform {

  require(inspections != null)

  import global._

  var dataDir: File = new File(".")
  var disabled: List[String] = Nil
  var ignoredFiles: List[String] = Nil
  var consoleOutput: Boolean = false
  var verbose: Boolean = false
  var debug: Boolean = false
  var summary: Boolean = true
  var disableXML = true
  var disableHTML = true
  var disableScalastyleXML = true
  var customInspections: Seq[Inspection] = Nil
  var diffBranch: Option[String] = None
  var diffFiles: List[String] = Nil

  private val count = new AtomicInteger(0)

  override val phaseName: String = "vigilance"
  override val runsAfter: List[String] = List("typer")
  override val runsBefore = List[String]("patmat")

  def disableAll: Boolean = disabled.exists(_.compareToIgnoreCase("all") == 0)

  def activeInspections: Seq[Inspection] = (inspections ++ customInspections)
    .filterNot(inspection => disabled.contains(inspection.getClass.getSimpleName))
  lazy val feedback = new Feedback(consoleOutput, global.reporter)

  override def newPhase(prev: scala.tools.nsc.Phase): Phase = new Phase(prev) {
    override def run(): Unit = {
      if (disableAll) {
        reporter.echo("[info] [vigilance] All inspections disabled")
      } else {
        reporter.echo(s"[info] [vigilance] ${activeInspections.size} activated inspections")
        if (verbose) {
          if (ignoredFiles.nonEmpty)
            reporter.echo(s"[info] [vigilance] $ignoredFiles ignored file patterns")
        }
        diffBranch.foreach { branch =>
          reporter.echo(s"[info] [vigilance] diff against [$branch]")
        }
        super.run()

        if (summary) {
          val errors = feedback.errors.size
          val warns = feedback.warns.size
          val infos = feedback.infos.size
          val level = if (errors > 0) "error" else if (warns > 0) "warn" else "info"
          reporter.echo(s"[$level] [vigilance] Analysis complete: ${count.get} files - $errors errors $warns warns $infos infos")
        }

        if (!disableHTML) {
          val html = IOUtils.writeHTMLReport(dataDir, feedback)
          if (verbose)
            reporter.echo(s"[info] [vigilance] Written HTML report [$html]")
        }
        if (!disableXML) {
          val xml = IOUtils.writeXMLReport(dataDir, feedback)
          if (verbose)
            reporter.echo(s"[info] [vigilance] Written XML report [$xml]")
        }
        if (!disableScalastyleXML) {
          val xml = IOUtils.writeScalastyleReport(dataDir, feedback)
          if (verbose)
            reporter.echo(s"[info] [vigilance] Written Scalastyle XML report [$xml]")
        }
      }
    }
  }

  protected def newTransformer(unit: CompilationUnit): Transformer = {
    count.incrementAndGet()
    new Transformer(unit)
  }

  class Transformer(unit: global.CompilationUnit) extends TypingTransformer(unit) {
    override def transform(tree: global.Tree): global.Tree = {
      if (ignoredFiles.exists(unit.source.path.matches)) {
        if (debug) {
          reporter.echo(s"[debug] Skipping vigilance [$unit]: ignored")
        }
      } else if (diffBranch.isDefined && !diffFiles.contains(unit.source.path)) {
        if (debug) {
          reporter.echo(s"[debug] Skipping vigilance [$unit]: not in diff")
        }
      } else {
        if (debug) {
          reporter.echo(s"[debug] Vigilance analysis [$unit] .....")
        }
        val context = InspectionContext(global, feedback)
        activeInspections.foreach(inspection => {
          val inspector = inspection.inspector(context)
          inspector.traverser.traverse(tree.asInstanceOf[inspector.context.global.Tree])
        })
      }
      tree
    }
  }
}
