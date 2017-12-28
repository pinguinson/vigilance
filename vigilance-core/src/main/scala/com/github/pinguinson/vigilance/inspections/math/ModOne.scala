package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

/**
  * @author Stephen Samuel
  *
  *         Inspired by http://findbugs.sourceforge.net/bugDescriptions.html#INT_BAD_REM_BY_1
  */
object ModOne extends Inspection {

  override val level = Levels.Warning
  override val description = "Integer mod one"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(Select(lhs, TermName("$percent")), List(Literal(Constant(1)))) if lhs.tpe <:< typeOf[Int] =>
          context.warn(tree.pos, self, "Any expression x % 1 will always return 0. " + tree.toString.take(300))
      }
    }
  }
}
