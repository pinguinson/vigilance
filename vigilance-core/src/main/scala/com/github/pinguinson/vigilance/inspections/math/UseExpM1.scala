package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

/** @author Matic Potočnik */
object UseExpM1 extends Inspection {

  override val level = Levels.Info
  override val description = "Use math.expm1"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {

        case Apply(Select(Apply(Select(pack, TermName("exp")), List(number)), nme.SUB), List(Literal(Constant(1)))) =>
          val math = pack.toString.stripSuffix(".`package`").substring(pack.toString.lastIndexOf('.'))
          context.warn(tree.pos, self, s"$math.expm1(x) is clearer and more performant than $math.exp(x) - 1")

      }
    }
  }
}
