package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
class ArraysInFormat extends Inspection { self =>

  override val level = Levels.Error
  override val description = "Incorrect number of args for format"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def containsArrayType(trees: List[Tree]) = trees.exists(_.tpe <:< typeOf[Array[_]])

      override def inspect(tree: Tree) = {
        case Apply(Select(lhs, TermName("format")), args) if containsArrayType(args) =>
          context.warn(tree.pos, self, tree.toString().take(500))
      }
    }
  }
}
