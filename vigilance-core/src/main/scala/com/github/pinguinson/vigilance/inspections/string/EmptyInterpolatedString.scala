package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object EmptyInterpolatedString extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Empty interpolated string"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        //TODO: select any?
        case Apply(Select(Apply(Select(_, TermName("apply")), _), TermName("s")), TermNil) =>
          context.warn(
            tree.pos,
            self,
            "String declared as interpolated but has no parameters: " + tree.toString().take(500))
      }
    }
  }
}