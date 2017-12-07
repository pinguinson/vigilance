package com.github.pinguinson.vigilance.inspections.unneccesary

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
class UnnecessaryToString extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Unnecessary toString"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._
      import definitions._

      override def inspect(tree: Tree) = {
        case Select(lhs, ToString) if lhs.tpe <:< StringTpe =>
          context.warn(tree.pos, self, "Calling toString method on String is redundant")
      }
    }
  }
}
