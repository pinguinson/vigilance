package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class FindDotIsDefined extends Inspection {

  override val level = Levels.Info
  override val description = "use exists() not find().isDefined()"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(Apply(Select(_, TermName("find")), _), TermName("isDefined")) =>
            context.warn(tree.pos, FindDotIsDefined.this, ".find(x => Bool).isDefined can be replaced with exists(x => Bool): " + tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}