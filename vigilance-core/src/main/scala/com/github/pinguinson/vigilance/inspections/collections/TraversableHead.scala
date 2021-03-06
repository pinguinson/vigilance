package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object TraversableHead extends Inspection {

  override val level = Levels.Error
  override val description = "Use of Traversable.head"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case SelectTraversableLike(Head) =>
          context.warn(tree.pos, self, "Traversable.head is unsafe, use Traversable.headOption instead")
      }
    }
  }
}