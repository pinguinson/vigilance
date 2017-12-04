package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

/**
 * @author Stephen Samuel
 *
 *         Inspired by http://codenarc.sourceforge.net/codenarc-rules-basic.html#BrokenOddnessCheck
 */
class BrokenOddness extends Inspection {

  override val level = Levels.Warning
  override val description = "Broken odd check"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(Apply(Select(_, TermName("$percent")), List(Literal(Constant(2)))),
            TermName("$eq$eq")), List(Literal(Constant(1)))) =>
            context.warn(tree.pos, BrokenOddness.this, "Consider using x % 2 != 0 for negative numbers" + tree.toString.take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}