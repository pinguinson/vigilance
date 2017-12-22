package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
object AvoidSizeEqualsZero extends Inspection {

  override val level = Levels.Warning
  override val description = "Avoid Traversable.size == 0"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val Size = TermName("size")
      private val Length = TermName("length")
      private val Traversable = typeOf[Traversable[_]]

      override def inspect(tree: Tree) = {
        case Apply(Select(Select(q, Size | Length), Equals), Zero) if q.tpe <:< Traversable =>
          context.warn(
            tree.pos,
            self,
            "Traversable.size is slow for some implementations. Prefer .isEmpty which is O(1): " + tree.toString.take(100)
          )
      }
    }
  }
}
