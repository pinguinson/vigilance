package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class TraversableHead extends Inspection {

  override val level = Levels.Error
  override val description = "Use of Traversable.head"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(left, TermName("head")) if left.tpe <:< typeOf[Traversable[_]] =>
            context.warn(tree.pos, TraversableHead.this, "Traversable.head is unsafe, use Traversable.headOption instead")
          case _ => continue(tree)
        }
      }
    }
  }
}