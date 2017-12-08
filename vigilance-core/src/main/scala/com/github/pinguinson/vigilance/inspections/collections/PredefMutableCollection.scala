package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
object PredefMutableCollection extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Use of mutable predefined collections"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case _: DefDef if tree.symbol.isAccessor =>
        case _: TypeTree if tree.tpe <:< typeOf[Seq[Any]] => warn(tree, "Seq")
        case _: TypeTree if tree.tpe <:< typeOf[Iterable[Any]] => warn(tree, "Iterable")
        case _: TypeTree if tree.tpe <:< typeOf[Traversable[Any]] => warn(tree, "Traversable")
      }

      def warn(tree: Tree, collection: String): Unit = {
        context.warn(
          tree.pos,
          self,
          s"$collection aliases scala.collection.mutable.$collection. Did you intend to use an immutable $collection?"
        )
      }
    }
  }
}