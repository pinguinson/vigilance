package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class UnsafeContains extends Inspection {

  override val level = Levels.Error
  override val description = "Unsafe contains"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {
      import context.global._
      import treeInfo.Applied

      private val Contains = TermName("contains")
      private val Seq = typeOf[Seq[_]].typeSymbol
      private def isSeq(tree: Tree): Boolean = tree.tpe.widen.baseClasses contains Seq
      private def isCompatibleType(container: Tree, value: Tree) = container.tpe baseType Seq match {
        case TypeRef(_, Seq, elem :: Nil) => value.tpe <:< elem
        case _                            => false
      }
      override def inspect(tree: Tree): Unit = tree match {
        case Applied(Select(lhs, Contains), _, (arg :: Nil) :: Nil) if isSeq(lhs) && !isCompatibleType(lhs, arg) =>
          context.warn(tree.pos, UnsafeContains.this, tree.toString().take(300))
        case _ =>
          continue(tree)
      }
    }
  }
}
