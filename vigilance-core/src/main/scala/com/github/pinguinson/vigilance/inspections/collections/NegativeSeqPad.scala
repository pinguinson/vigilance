package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class NegativeSeqPad extends Inspection {

  override val level = Levels.Error
  override val description = "Negative seq padTo"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(TypeApply(Select(lhs, TermName("padTo")), _), Literal(Constant(x)) :: tail) =>
            context.warn(tree.pos, NegativeSeqPad.this, tree.toString.take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}