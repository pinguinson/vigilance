package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object ArraysToString extends Inspection {

  override val level = Levels.Warning
  override val description = "Use of Array.toString"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def isArray(tree: Tree) = tree.tpe <:< typeOf[Array[_]]

      override def inspect(tree: Tree) = {
        case Apply(Select(lhs, ToString), TermNil) if isArray(lhs) =>
          context.warn(tree.pos, self, "toString on an array does not perform a deep toString: " + tree.toString.take(500))
      }
    }
  }
}