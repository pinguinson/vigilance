package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

class TraversableLast extends Inspection {

  override val level = Levels.Error
  override val description = "Use of Traversable.last"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(left, TermName("last")) if left.tpe <:< typeOf[Traversable[_]] =>
              context.warn(tree.pos, TraversableLast.this, "Traversable.last is unsafe, use Traversable.lastOption instead")
          case _ => continue(tree)
        }
      }
    }
  }
}
