package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

import scala.runtime.{RichInt, RichLong}

/** @author Stephen Samuel */
class AvoidToMinusOne extends Inspection {

  override val level = Levels.Info
  override val description = "Avoid To Minus One"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._
      import definitions._

      private val Foreach = TermName("foreach")
      private val Minus = TermName("$minus")
      private val To = TermName("to")

      private def isIntegral(tree: Tree): Boolean = {
        tree.tpe <:< IntTpe || tree.tpe <:< LongTpe || tree.tpe <:< typeOf[RichInt] || tree.tpe <:< typeOf[RichLong]
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(TypeApply(Select(Apply(Select(lhs, To),
            List(Apply(Select(loopvar, Minus), List(Literal(Constant(1)))))), Foreach), _), _) if isIntegral(lhs) && isIntegral(loopvar) =>
            context.warn(tree.pos, AvoidToMinusOne.this, "j to k - 1 can be better written as j until k: " + tree.toString().take(200))
          case _ => continue(tree)
        }
      }
    }
  }
}

