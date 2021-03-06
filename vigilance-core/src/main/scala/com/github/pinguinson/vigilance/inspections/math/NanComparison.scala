package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object NanComparison extends Inspection {

  override val level = Levels.Error
  override val description = "NaN comparision"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._
      import definitions._

      private def isNan(value: Any): Boolean = {
        value match {
          case d: Double => d.isNaN
          case _ => false
        }
      }

      override def inspect(tree: Tree) = {
        case Apply(Select(lhs, Equals), List(Literal(Constant(x)))) if isFloatingPointType(lhs) && isNan(x) =>
          warn(tree)
        case Apply(Select(Literal(Constant(x)), Equals), List(rhs)) if isFloatingPointType(rhs) && isNan(x) =>
          warn(tree)
      }

      private def isFloatingPointType(lhs: Tree): Boolean = {
        lhs.tpe <:< DoubleClass.tpe || lhs.tpe <:< FloatClass.tpe
      }

      private def warn(tree: Tree) {
        context.warn(tree.pos, self, "Comparison to NaN will always fail")
      }
    }
  }
}
