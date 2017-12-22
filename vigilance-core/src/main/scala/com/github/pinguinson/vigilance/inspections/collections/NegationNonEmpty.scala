package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object NegationNonEmpty extends Inspection {

  override val level = Levels.Info
  override val description = "!nonEmpty can be replaced with isEmpty"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Select(SelectTraversable(NonEmpty), Bang) =>
          context.warn(tree.pos, self, description)
      }
    }
  }
}
