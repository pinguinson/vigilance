package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object BigDecimalScaleWithoutRoundingMode extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "BigDecimal setScale() without rounding mode"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def isBigDecimal(t: Tree) = t.tpe <:< typeOf[BigDecimal] || t.tpe <:< typeOf[java.math.BigDecimal]

      override def inspect(tree: Tree) = {
        case Apply(Select(lhs, TermName("setScale")), List(arg)) if isBigDecimal(lhs) =>
          context.warn(tree.pos, self, tree.toString.take(500))
      }
    }
  }
}