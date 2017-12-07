package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class NegationIsEmpty extends Inspection { self =>

  override val level = Levels.Info
  override val description = "!isEmpty can be replaced with nonEmpty"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val IsEmpty = TermName("isEmpty")
      private val Bang = TermName("unary_$bang")

      //TODO: is it only applicable to Traversable? How about Option?
      override def inspect(tree: Tree) = {
        case Select(SelectTraversable(IsEmpty), Bang) =>
          context.warn(tree.pos, self, description)
      }
    }
  }
}
