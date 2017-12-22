package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
object NegationIsEmpty extends Inspection {

  override val level = Levels.Info
  override val description = "!isEmpty and !nonEmpty usage"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      //TODO: add more cases
      override def inspect(tree: Tree) = {
        case Select(SelectTraversable(IsEmpty), Bang) =>
          context.warn(tree.pos, self, "Traversable.!isEmpty can be replaced with Traversable.nonEmpty")
        case Select(SelectTraversable(NonEmpty), Bang) =>
          context.warn(tree.pos, self, "Traversable.!nonEmpty can be replaced with Traversable.isEmpty")
        case Select(SelectOption(IsEmpty), Bang) =>
          context.warn(tree.pos, self, "Option.!isEmpty can be replaced with Option.nonEmpty")
        case Select(SelectOption(IsEmpty), Bang) =>
          context.warn(tree.pos, self, "Option.!nonEmpty can be replaced with Option.isEmpty")
      }
    }
  }
}
