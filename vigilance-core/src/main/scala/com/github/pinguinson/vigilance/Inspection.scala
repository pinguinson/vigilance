package com.github.pinguinson.vigilance

import com.github.pinguinson.vigilance.util._

import scala.annotation.tailrec
import scala.reflect.internal.util.Position
import scala.tools.nsc.Global

/** @author Stephen Samuel */
trait Inspection {
  def inspector(context: InspectionContext): Inspector
  def level: Level
  def description: String
}

abstract class Inspector(val context: InspectionContext) {
  def traverser: context.Traverser
}

case class InspectionContext(global: Global, feedback: Feedback) extends CompilerAccess with Constants with Extractors {

  def warn(pos: Position, inspection: Inspection, snippet: String): Unit = {
    feedback.warn(pos, inspection, snippet)
  }

  trait Traverser extends global.Traverser {

    import global._

    private val SuppressWarnings = typeOf[SuppressWarnings]
    private val Safe = typeOf[Safe]

    @tailrec
    private def inspectionClass(cls: Class[_]): Class[_] = Option(cls.getEnclosingClass) match {
      case None    => cls
      case Some(c) => inspectionClass(c)
    }

    private def isAllDisabled(an: AnnotationInfo): Boolean = {
      an.javaArgs.head._2.toString.toLowerCase.contains("\"all\"")
    }

    private def isThisDisabled(an: AnnotationInfo): Boolean = {
      an.javaArgs.head._2.toString.toLowerCase.contains(inspectionClass(getClass).getSimpleName.toLowerCase)
    }

    private def isSkipAnnotation(an: AnnotationInfo): Boolean = {
      an.tree.tpe =:= SuppressWarnings || an.tree.tpe =:= Safe
    }

    private def isSuppressed(symbol: Symbol): Boolean = {
      symbol != null && symbol.annotations.exists(an => isSkipAnnotation(an) && (isAllDisabled(an) || isThisDisabled(an)))
    }

    protected def continue(tree: Tree): Unit = super.traverse(tree)

    protected def inspect(tree: Tree): PartialFunction[Tree, Unit]

    override final def traverse(tree: Tree): Unit = tree match {
        // ignore synthetic methods added
        // TODO: can we replace these with `case t: Tree if isSuppressed(t.symbol)`?
        case ddf: DefDef    if isSuppressed(ddf.symbol) || tree.symbol.isSynthetic =>
        case blk: Block     if isSuppressed(blk.symbol) =>
        case iff: If        if isSuppressed(iff.symbol) =>
        case tri: Try       if isSuppressed(tri.symbol) =>
        case mod: ModuleDef if isSuppressed(mod.symbol) =>
        case ClassDef(_, _, _, Template(parents, _, _)) if parents.map(_.tpe.typeSymbol.fullName).contains("scala.reflect.api.TypeCreator") =>
        case cdf: ClassDef  if isSuppressed(cdf.symbol) =>
        case _ if analyzer.hasMacroExpansionAttachment(tree) => //skip macros as per http://bit.ly/2uS8BrU
        case _ => inspect(tree).applyOrElse(tree, continue)
      }
    }
  }

