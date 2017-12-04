package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class EmptyInterpolatedString extends Inspection {

  override val level = Levels.Warning
  override val description = "Empty interpolated string"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(Apply(Select(lhs, TermName("apply")), List(string)), TermName("s")), Nil) =>
            context.warn(
              tree.pos,
              EmptyInterpolatedString.this,
              "String declared as interpolated but has no parameters: " + tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}