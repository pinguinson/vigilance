package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
class AvoidSizeNotEqualsZero extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Avoid Traversable.size == 0"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val Size = TermName("size")
      private val Length = TermName("length")
      private val NotEquals = TermName("$bang$eq")
      private val Zero = List(Literal(Constant(0)))
      private val Traversable = typeOf[Traversable[_]]

      override def inspect(tree: Tree) = {
        case Apply(Select(Select(lhs, Length | Size), NotEquals), Zero) if lhs.tpe <:< Traversable =>
          context.warn(tree.pos, self, "Traversable.size might be slow, use .nonEmpty instead")
      }
    }
  }
}
