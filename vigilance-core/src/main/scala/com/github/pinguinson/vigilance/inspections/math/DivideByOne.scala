package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class DivideByOne extends Inspection {

  override val level = Levels.Warning
  override val description = "Division by one"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private def isNumber(tree: Tree) = {
        tree.tpe <:< typeOf[Int] ||
          tree.tpe <:< typeOf[Long] ||
          tree.tpe <:< typeOf[Double] ||
          tree.tpe <:< typeOf[Float]
      }

      private def isOne(value: Any): Boolean = value match {
        case i: Int => i == 1
        case _      => false
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(lhs, TermName("$div")), List(Literal(Constant(x)))) if isNumber(lhs) && isOne(x) =>
            context.warn(tree.pos, DivideByOne.this, tree.toString.take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}
