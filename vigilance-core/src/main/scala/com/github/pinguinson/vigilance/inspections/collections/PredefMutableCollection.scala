package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
object PredefMutableCollection extends Inspection {

  //FIXME: doesn't seem to be working as expected
  override val level = Levels.Info
  override val description = "Use of mutable predefined collections"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case DefDef(_, _, _, _, _, _) if tree.symbol.isAccessor =>
        case TypeTree() if tree.tpe.erasure.toString == "Seq[Any]" => warn(tree, "Seq")
        case TypeTree() if tree.tpe.erasure.toString == "Iterable[Any]" => warn(tree, "Iterable")
        case TypeTree() if tree.tpe.erasure.toString == "Traversable[Any]" => warn(tree, "Traversable")
      }

      def warn(tree: Tree, collection: String): Unit = {
        context.warn(tree.pos, self, s"$collection aliases scala.collection.mutable.$collection. Did you intend to use an immutable $collection?")
      }
    }
  }
}