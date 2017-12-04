package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class PreferSetEmpty extends Inspection {

  override val level = Levels.Warning
  override val description = "Prefer Set.empty"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val SetTerm = TermName("Set")
      private val ApplyTerm = TermName("apply")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(TypeApply(Select(Select(_, SetTerm), ApplyTerm), _), List()) =>
            context.warn(
              tree.pos,
              PreferSetEmpty.this,
              "Set[T]() creates a new instance. Consider Set.empty which does not allocate a new object. " + tree.toString.take(500)
            )
          case _ => continue(tree)
        }
      }
    }
  }
}