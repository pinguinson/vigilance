package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class BigDecimalScaleWithoutRoundingMode extends Inspection {

  override val level = Levels.Warning
  override val description = "BigDecimal setScale() without rounding mode"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private def isBigDecimal(t: Tree) = t.tpe <:< typeOf[BigDecimal] || t.tpe <:< typeOf[java.math.BigDecimal]

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(lhs, TermName("setScale")), List(arg)) if isBigDecimal(lhs) =>
            context.warn(tree.pos, BigDecimalScaleWithoutRoundingMode.this, tree.toString.take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}