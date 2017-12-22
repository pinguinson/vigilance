package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object UnsafeContains extends Inspection {

  override val level = Levels.Error
  override val description = "Unsafe contains"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val Seq = typeOf[Seq[_]].typeSymbol

      private def isSeq(tree: Tree): Boolean = tree.tpe.widen.baseClasses contains Seq

      private def isCompatibleType(container: Tree, value: Tree) = container.tpe baseType Seq match {
        case TypeRef(_, Seq, elem :: TermNil) => value.tpe <:< elem
        case _                            => false
      }

      override protected def inspect(tree: Tree) = {
        case treeInfo.Applied(Select(lhs, Contains), _, (arg :: TermNil) :: TermNil) if isSeq(lhs) && !isCompatibleType(lhs, arg) =>
          context.warn(tree.pos, self, tree.toString.take(300))
      }
    }
  }
}
