package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

object FilterOperations extends Inspection {

  override val level = Levels.Info
  override val description = "Use of .filter"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val filter = s".filter(predicate)"

      private def warn(tree: Tree, comment: String): Unit = {
        context.warn(tree.pos, self, comment)
      }
      
      override def inspect(tree: Tree) = {
        case Select(Apply(Select(_, Filter), _), Head) =>
          warn(tree, s"$filter.head can be replaced with .find(predicate) and a match")
        case Select(Apply(Select(_, Filter), _), HeadOption) =>
          warn(tree, s"$filter.headOption can be replaced with .find(predicate)")
        case Select(Apply(Select(_, Filter), _), IsEmpty) =>
          warn(tree, s"$filter.isEmpty can be replaced with .forall(!predicate)")
        case Select(Apply(Select(_, Filter), _), Size) =>
          warn(tree, s"$filter.size can be replaced with .count(predicate)")
      }
    }
  }
}
