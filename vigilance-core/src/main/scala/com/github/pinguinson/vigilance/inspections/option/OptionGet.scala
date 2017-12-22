package com.github.pinguinson.vigilance.inspections.option

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object OptionGet extends Inspection {

  override val level = Levels.Error
  override val description = "Use of Option.get"

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override protected def inspect(tree: Tree) = {
        case SelectOption(Get) => context.warn(tree.pos, self, "Use of Option.get is discouraged, consider using .getOrElse instead")
      }
    }
  }
}
