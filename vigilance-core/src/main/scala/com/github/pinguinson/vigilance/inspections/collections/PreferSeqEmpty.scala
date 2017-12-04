package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class PreferSeqEmpty extends Inspection {

  override val level = Levels.Info
  override val description = "Prefer Seq.empty"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val ApplyTerm = TermName("apply")
      private val SeqTerm = TermName("Seq")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(TypeApply(Select(Select(_, SeqTerm), ApplyTerm), _), List()) =>
            context.warn(
              tree.pos,
              PreferSeqEmpty.this,
              "Seq[T]() creates a new instance. Consider Seq.empty which does not allocate a new object. " + tree.toString.take(500)
            )
          case _ => continue(tree)
        }
      }
    }
  }
}