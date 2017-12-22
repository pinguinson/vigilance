package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object NegativeSeqPad extends Inspection {

  override val level = Levels.Error
  override val description = "Negative seq padTo"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(TypeApply(Select(_, TermName("padTo")), _), Literal(Constant(x: Int)) :: tail) if x < 0 =>
          context.warn(tree.pos, self, tree.toString.take(500))
      }
    }
  }
}