package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class DoubleNegation extends Inspection {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def postTyperTraverser = Some apply new context.Traverser {

      import context.global._

      private val Bang = TermName("unary_$bang")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(Select(_, Bang), Bang) =>
            context.warn("Double negation",
              tree.pos,
              Levels.Info,
              "Double negation can be removed: " + tree.toString().take(200),
              DoubleNegation.this)
          case _ => continue(tree)
        }
      }
    }
  }
}

