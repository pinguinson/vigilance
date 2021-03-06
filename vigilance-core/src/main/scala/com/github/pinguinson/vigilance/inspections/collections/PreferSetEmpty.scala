package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object PreferSetEmpty extends Inspection {

  override val level = Levels.Warning
  override val description = "Prefer Set.empty"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val SetTerm = TermName("Set")
      private val ApplyTerm = TermName("apply")

      override def inspect(tree: Tree) = {
        case Apply(TypeApply(Select(Select(_, SetTerm), ApplyTerm), _), List()) =>
          context.warn(tree.pos, self, "Set[T]() creates a new instance, use Set.empty[T] which does not allocate a new object")
      }
    }
  }
}