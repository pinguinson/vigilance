package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class ArraysToString extends Inspection {

  override val level = Levels.Warning
  override val description = "Use of Array.toString"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val ToString = TermName("toString")
      private def isArray(tree: Tree) = tree.tpe <:< typeOf[Array[_]]

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(lhs, ToString), Nil) if isArray(lhs) =>
            context.warn(tree.pos, ArraysToString.this, "toString on an array does not perform a deep toString: " + tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}