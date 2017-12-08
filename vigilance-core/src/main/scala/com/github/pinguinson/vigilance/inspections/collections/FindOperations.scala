package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

object FindOperations extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Use of .find"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(Select(Apply(Select(_, Find), _), NotEquals), List(Select(_, TermName("None")))) =>
          context.warn(tree.pos, self, ".find(predicate) != None can be replaced with .exists")
        case Select(Apply(Select(_, Find), _), IsDefined) =>
          context.warn(tree.pos, self, ".find(x => Bool).isDefined can be replaced with exists(x => Bool)")
        case Select(Apply(Select(_, Find), _), NonEmpty) =>
          context.warn(tree.pos, self, ".find(x => Bool).nonEmpty can be replaced with exists(x => Bool)")
      }
    }
  }
}