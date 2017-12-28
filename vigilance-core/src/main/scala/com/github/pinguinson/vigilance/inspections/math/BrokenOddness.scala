package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

/**
  * @author Stephen Samuel
  *
  *         Inspired by http://codenarc.sourceforge.net/codenarc-rules-basic.html#BrokenOddnessCheck
  */
object BrokenOddness extends Inspection {

  override val level = Levels.Warning
  override val description = "Broken odd check"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(Select(Apply(Select(_, TermName("$percent")), List(Literal(Constant(2)))),
        Equals), List(Literal(Constant(1)))) =>
          context.warn(tree.pos, self, "Consider using x % 2 != 0 for negative numbers" + tree.toString.take(500))
      }
    }
  }
}