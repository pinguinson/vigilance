package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object DivideByOne extends Inspection {

  override val level = Levels.Warning
  override val description = "Division by one"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def isNumber(tree: Tree) = {
        tree.tpe <:< typeOf[Int] ||
          tree.tpe <:< typeOf[Long] ||
          tree.tpe <:< typeOf[Double] ||
          tree.tpe <:< typeOf[Float]
      }

      //TODO: fix this
      override def inspect(tree: Tree) = {
        case Apply(Select(lhs, TermName("$div")), List(Literal(Constant(1)))) if isNumber(lhs) =>
          context.warn(tree.pos, self, "Division by 1 is unnecessary")
      }
    }
  }
}
