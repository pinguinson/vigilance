package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
class DoubleNegation extends Inspection {

  override val level = Levels.Info
  override val description = "Double negation"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Bang = TermName("unary_$bang")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(Select(_, Bang), Bang) =>
            context.warn(tree.pos, DoubleNegation.this, "Double negation can be removed: " + tree.toString().take(200))
          case _ => continue(tree)
        }
      }
    }
  }
}
