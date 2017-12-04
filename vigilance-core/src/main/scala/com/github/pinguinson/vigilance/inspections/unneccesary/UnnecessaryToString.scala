package com.github.pinguinson.vigilance.inspections.unneccesary

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class UnnecessaryToString extends Inspection {

  override val level = Levels.Warning
  override val description = "Unnecessary toString"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._
      import definitions._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(lhs, TermName("toString")) if lhs.tpe <:< StringClass.tpe =>
            context.warn(tree.pos, UnnecessaryToString.this, tree.toString().take(200))
          case _ =>
        }
        continue(tree)
      }
    }
  }
}
