package com.github.pinguinson.vigilance.inspections.style

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class SimplifyBooleanExpression extends Inspection {

  override val level = Levels.Info
  override val description = "Simplify boolean expressions"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {

    override def traverser = new context.Traverser {

      import context.global._

      private val Equals = TermName("$eq$eq")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(lhs, Equals), List(Literal(Constant(false)))) =>
            context.warn(tree.pos, SimplifyBooleanExpression.this, "Boolean expressions such as x == false can be re-written as !x: " + tree.toString().take(200))
          case _ => continue(tree)
        }
      }
    }
  }
}
