package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

object TraversableLast extends Inspection {

  override val level = Levels.Error
  override val description = "Use of Traversable.last"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case SelectTraversableLike(Last) =>
          context.warn(tree.pos, self, "Traversable.last is unsafe, use Traversable.lastOption instead")
      }
    }
  }
}
