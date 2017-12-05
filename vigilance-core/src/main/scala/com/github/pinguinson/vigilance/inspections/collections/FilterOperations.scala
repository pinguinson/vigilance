package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

class FilterOperations extends Inspection {

  override val level = Levels.Info
  override val description = "Use of Traversable.filter"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Filter = TermName("filter")
      private val Head = TermName("head")
      private val HeadOption = TermName("headOption")
      private val IsEmpty = TermName("isEmpty")
      private val Size = TermName("size")

      private val filter = s"Traversable.filter(predicate)"

      private def warn(tree: Tree, comment: String): Unit = {
        context.warn(tree.pos, FilterOperations.this, comment)
      }
      
      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(Apply(Select(_, Filter), _), Head) =>
            warn(tree, s"$filter.head can be replaced with Traversable.find(predicate) and a match")
          case Select(Apply(Select(_, Filter), _), HeadOption) =>
            warn(tree, s"$filter.headOption can be replaced with Traversable.find(predicate)")
          case Select(Apply(Select(_, Filter), _), IsEmpty) =>
            warn(tree, s"$filter.isEmpty can be replaced with Traversable.forall(!predicate)")
          case Select(Apply(Select(_, Filter), _), Size) =>
            warn(tree, s"$filter.size can be replaced with Traversable.count(predicate)")
          case _ => continue(tree)
        }
      }
    }
  }
}
