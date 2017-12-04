package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class FilterHeadOption extends Inspection {

  override val level = Levels.Info
  override val description = "Use if filter(...).headOption"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(Apply(Select(_, TermName("filter")), _), TermName("headOption")) =>
            context.warn(tree.pos, FilterHeadOption.this, ".filter(x => Bool).headOption can be replaced with find(x => Bool): " + tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}