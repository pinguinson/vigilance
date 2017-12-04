package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class NegationIsEmpty extends Inspection {

  override val level = Levels.Info
  override val description = "!isEmpty can be replaced with nonEmpty"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val IsEmpty = TermName("isEmpty")
      private val Bang = TermName("unary_$bang")
      private def isTraversable(tree: Tree) = tree.tpe <:< typeOf[Traversable[_]]

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(Select(lhs, IsEmpty), Bang) if isTraversable(lhs) =>
            context.warn(tree.pos, NegationIsEmpty.this, tree.toString.take(100))
          case _ => continue(tree)
        }
      }
    }
  }
}