package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance.{ Levels, Inspector, InspectionContext, Inspection }

/** @author Stephen Samuel */
class ZeroNumerator extends Inspection {

  override val level = Levels.Warning
  override val description = "Zero numerator"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(Literal(Constant(0)), TermName("$div")), _) =>
            context.warn(tree.pos, ZeroNumerator.this, "Dividing zero by any number will always return zero")
          case _ => continue(tree)
        }
      }
    }
  }
}
