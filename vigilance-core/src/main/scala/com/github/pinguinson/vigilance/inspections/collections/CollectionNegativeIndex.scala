package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class CollectionNegativeIndex extends Inspection {

  override val level = Levels.Warning
  override val description = "Collection index out of bounds"

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(lhs, TermName("apply")), List(Literal(Constant(x: Int)))) if lhs.tpe <:< typeOf[List[_]] && x < 0 =>
            context.warn(tree.pos, CollectionNegativeIndex.this, "Collections don't have items with negative indices")
          case _ => continue(tree)
        }
      }
    }
  }
}
