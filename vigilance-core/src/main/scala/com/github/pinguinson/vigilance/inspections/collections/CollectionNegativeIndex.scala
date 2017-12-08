package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object CollectionNegativeIndex extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Collection index out of bounds"

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(SelectList(TermApply), List(Literal(Constant(x: Int)))) if x < 0 =>
          context.warn(tree.pos, self, "Collections don't have items with negative indices")
      }
    }
  }
}
