package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class ComparisonToEmptyList extends Inspection {

  override val level = Levels.Info
  override val description = "Comparison to empty list"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Equals = TermName("$eq$eq")
      private val Empty = TermName("empty")
      private val Nil = TermName("Nil")
      private val TermList = TermName("List")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(_, Equals), List(Select(_, Nil))) => warn(tree)
          case Apply(Select(Select(_, Nil), Equals), _) => warn(tree)
          case Apply(Select(_, Equals), List(TypeApply(Select(Select(_, TermList), Empty), _))) => warn(tree)
          case Apply(Select(TypeApply(Select(Select(_, TermList), Empty), _), Equals), _) => warn(tree)
          case _ => continue(tree)
        }
      }

      private def warn(tree: Tree) {
        context.warn(tree.pos, ComparisonToEmptyList.this, "Prefer use of isEmpty instead of comparison to an empty List: " + tree.toString().take(200))
      }
    }
  }
}