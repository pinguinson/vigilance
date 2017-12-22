package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object DoubleNegation extends Inspection {

  override val level = Levels.Info
  override val description = "Double negation"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Select(Select(_, Bang), Bang) =>
          context.warn(tree.pos, self, "Double negation can be removed")
      }
    }
  }
}

