package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
object ComparisonToEmptySet extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Comparison to empty list"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(Select(_, Equals), List(Apply(TypeApply(Select(Select(_, TermSet), TermApply), _), _))) => warn(tree)
        case Apply(Select(Apply(TypeApply(Select(Select(_, TermSet), TermApply), _), _), Equals), _) => warn(tree)
        case Apply(Select(_, Equals), List(TypeApply(Select(Select(_, TermSet), Empty), _))) => warn(tree)
        case Apply(Select(TypeApply(Select(Select(_, TermSet), Empty), _), Equals), _) => warn(tree)
      }

      private def warn(tree: Tree) {
        context.warn(tree.pos, self, "Prefer use of isEmpty instead of comparison to an empty List")
      }
    }
  }
}