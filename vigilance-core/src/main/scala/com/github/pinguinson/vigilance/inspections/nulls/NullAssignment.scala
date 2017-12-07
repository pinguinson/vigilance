package com.github.pinguinson.vigilance.inspections.nulls

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class NullAssignment extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Null assignment"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def warn(tree: Tree) {
        context.warn(tree.pos, self, "Null assignment on line " + tree.pos.line)
      }

      override def inspect(tree: Tree) = {
        case ValDef(_, _, _, Null) =>
          warn(tree)
        case Apply(Select(_, name), List(Null)) if name.endsWith("_$eq") =>
          warn(tree)
        case Assign(_, Null) =>
          warn(tree)
      }
    }
  }
}