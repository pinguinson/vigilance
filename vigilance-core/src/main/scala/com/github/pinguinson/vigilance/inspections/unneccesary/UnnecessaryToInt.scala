package com.github.pinguinson.vigilance.inspections.unneccesary

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
class UnnecessaryToInt extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Unnecessary toInt"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._
      import definitions._

      override def inspect(tree: Tree) = {
        case Select(lhs, TermName("toInt")) if lhs.tpe <:< IntClass.tpe =>
          context.warn(tree.pos, self, "Calling toInt method on Int is redundant")
      }
    }
  }
}
