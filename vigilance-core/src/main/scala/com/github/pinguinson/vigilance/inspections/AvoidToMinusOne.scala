package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

import scala.runtime.{RichInt, RichLong}

/** @author Stephen Samuel */
object AvoidToMinusOne extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Avoid To Minus One"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._
      import context.global.definitions._

      private def isIntegral(tree: Tree): Boolean = {
        tree.tpe <:< IntTpe || tree.tpe <:< LongTpe || tree.tpe <:< typeOf[RichInt] || tree.tpe <:< typeOf[RichLong]
      }

      override def inspect(tree: Tree) = {
        case Apply(TypeApply(Select(Apply(Select(lhs, To),
        List(Apply(Select(loopvar, Minus), List(One)))), Foreach), _), _) if isIntegral(lhs) && isIntegral(loopvar) =>
          context.warn(tree.pos, self, "j to k - 1 can be better written as j until k: " + tree.toString().take(200))
      }
    }
  }
}

