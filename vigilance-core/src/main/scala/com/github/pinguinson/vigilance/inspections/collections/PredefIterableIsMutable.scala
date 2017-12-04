package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class PredefIterableIsMutable extends Inspection {

  override val level = Levels.Info
  override val description = "Default Iterable is mutable"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case DefDef(mods, _, _, _, _, _) if tree.symbol.isAccessor =>
          case TypeTree() if tree.tpe.erasure.toString() == "Iterable[Any]" => warn(tree)
          case _ => continue(tree)
        }
      }

      def warn(tree: Tree): Unit = {
        context.warn(tree.pos, PredefIterableIsMutable.this, "Iterable aliases scala.collection.mutable.Iterable. Did you intend to use an immutable Iterable?")
      }
    }
  }
}