package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class PredefTraversableIsMutable extends Inspection {

  override val level = Levels.Info
  override val description = "Default Traversable is mutable"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case _: TypeTree if tree.tpe <:< typeOf[Traversable[_]] => warn(tree)
          case _ => continue(tree)
        }
      }

      def warn(tree: Tree): Unit = {
        context.warn(
          tree.pos,
          PredefTraversableIsMutable.this,
          "Traversable aliases scala.collection.mutable.Traversable. Did you intend to use an immutable Traversable?"
        )
      }
    }
  }
}