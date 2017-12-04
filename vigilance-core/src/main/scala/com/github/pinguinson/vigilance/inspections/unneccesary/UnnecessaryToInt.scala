package com.github.pinguinson.vigilance.inspections.unneccesary

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class UnnecessaryToInt extends Inspection {

  override val level = Levels.Warning
  override val description = "Unnecessary toInt"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._
      import definitions._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(lhs, TermName("toInt")) if lhs.tpe <:< IntClass.tpe =>
            context.warn(tree.pos, UnnecessaryToInt.this, "Unnecessary invocation of toInt on instance of Int " + tree.toString().take(200))
          case _ =>
        }
        continue(tree)
      }
    }
  }
}
