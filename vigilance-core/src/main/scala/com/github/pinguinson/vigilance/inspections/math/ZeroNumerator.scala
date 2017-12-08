package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance.{Levels, Inspector, InspectionContext, Inspection}

/** @author Stephen Samuel */
object ZeroNumerator extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Zero numerator"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(Select(Literal(Constant(0)), TermName("$div")), _) =>
          context.warn(tree.pos, self, "Dividing zero by any number will always return zero")
      }
    }
  }
}
