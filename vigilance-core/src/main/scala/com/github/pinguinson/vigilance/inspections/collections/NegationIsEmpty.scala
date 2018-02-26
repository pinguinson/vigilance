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
        case Select(SelectTraversableLike(IsEmpty), Bang) =>
          context.warn(tree.pos, self, "Traversable.!isEmpty can be replaced with Traversable.nonEmpty")
        case Select(SelectTraversableLike(NonEmpty), Bang) =>
          context.warn(tree.pos, self, "Traversable.!nonEmpty can be replaced with Traversable.isEmpty")
        case Select(SelectOption(IsEmpty), Bang) =>
          context.warn(tree.pos, self, "Option.!isEmpty can be replaced with Option.nonEmpty or Option.isDefined")
        case Select(SelectOption(NonEmpty), Bang) =>
          context.warn(tree.pos, self, "Option.!nonEmpty can be replaced with Option.isEmpty")
        case Select(SelectOption(IsDefined), Bang) =>
          context.warn(tree.pos, self, "Option.!isDefined can be replaced with Option.isEmpty")
      }
    }
  }
}
