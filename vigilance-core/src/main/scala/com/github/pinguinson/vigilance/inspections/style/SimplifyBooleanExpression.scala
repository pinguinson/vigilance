package com.github.pinguinson.vigilance.inspections.style

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object SimplifyBooleanExpression extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Simplify boolean expressions"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {

    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(Select(lhs, Equals), List(False)) =>
          context.warn(tree.pos, self, "Boolean expressions such as x == false can be re-written as !x: " + tree.toString().take(200))
      }
    }
  }
}
