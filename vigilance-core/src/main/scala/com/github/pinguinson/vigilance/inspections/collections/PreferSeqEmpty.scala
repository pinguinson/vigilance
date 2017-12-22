package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object PreferSeqEmpty extends Inspection {

  override val level = Levels.Info
  override val description = "Prefer Seq.empty"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val ApplyTerm = TermName("apply")
      private val SeqTerm = TermName("Seq")

      override def inspect(tree: Tree) = {
        case Apply(TypeApply(Select(Select(_, SeqTerm), ApplyTerm), _), List()) =>
          context.warn(
            tree.pos,
            self,
            "Seq[T]() creates a new instance. Consider Seq.empty which does not allocate a new object. " + tree.toString.take(500)
          )
      }
    }
  }
}