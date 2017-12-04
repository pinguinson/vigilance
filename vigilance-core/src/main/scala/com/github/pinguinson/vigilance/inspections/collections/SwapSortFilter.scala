package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class SwapSortFilter extends Inspection {

  override val level = Levels.Warning
  override val description = "Swap sort filter"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Seq = typeOf[Seq[_]]
      private def isSeq(tree: Tree) = tree.tpe <:< Seq

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(Apply(TypeApply(Select(lhs, TermName("sorted")), _), _), TermName("filter")), _) if isSeq(lhs) =>
            warn(tree)
          case Apply(Select(Apply(Apply(TypeApply(Select(lhs, TermName("sortBy")), _), _), _), TermName("filter")), _) if isSeq(lhs) =>
            warn(tree)
          case Apply(Select(Apply(Select(lhs, TermName("sortWith")), _), TermName("filter")), _) if isSeq(lhs) =>
            warn(tree)
          case _ => continue(tree)
        }
      }

      private def warn(tree: Tree): Unit = {
        context.warn(tree.pos, SwapSortFilter.this, "Replace sort.filter with filter.sort for better performance: " + tree.toString.take(500))
      }
    }
  }
}
