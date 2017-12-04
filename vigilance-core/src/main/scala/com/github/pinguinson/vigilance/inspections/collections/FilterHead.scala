package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class FilterHead extends Inspection {

  override val level = Levels.Info
  override val description = "Use of .filter(...).head"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Filter = TermName("filter")
      private val Head = TermName("head")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(Apply(Select(_, Filter), _), Head) =>
            context.warn(tree.pos, FilterHead.this, ".filter(x => Bool).head can be replaced with find(x => Bool) and a match: " + tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}