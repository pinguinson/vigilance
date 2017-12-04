package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Josh Rosen */
class CollectionIndexOnNonIndexedSeq extends Inspection {

  override val level = Levels.Warning
  override val description = "Seq.apply on a non-IndexedSeq may cause performance problems"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private def isSeq(t: Tree) = t.tpe <:< typeOf[Seq[Any]]
      private def isIndexedSeq(t: Tree) = t.tpe <:< typeOf[IndexedSeq[Any]]
      private def isLiteral(t: Tree) = t match {
        case Literal(_) => true
        case _ => false
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(lhs, TermName("apply")), List(idx)) if isSeq(lhs) && !isIndexedSeq(lhs) && !isLiteral(idx) =>
            context.warn(
              tree.pos,
              CollectionIndexOnNonIndexedSeq.this,
              tree.toString().take(100)
            )
          case _ => continue(tree)
        }
      }
    }
  }
}
