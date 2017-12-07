package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class SwapSortFilter extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Swap sort filter"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(Select(Apply(TypeApply(SelectSeq(TermName("sorted")), _), _), Filter), _) =>
          warn(tree)
        case Apply(Select(Apply(Apply(TypeApply(SelectSeq(TermName("sortBy")), _), _), _), Filter), _) =>
          warn(tree)
        case Apply(Select(Apply(SelectSeq(TermName("sortWith")), _), Filter), _) =>
          warn(tree)
      }

      private def warn(tree: Tree): Unit = {
        context.warn(tree.pos, self, "Replace sort.filter with filter.sort for better performance: " + tree.toString.take(500))
      }
    }
  }
}
