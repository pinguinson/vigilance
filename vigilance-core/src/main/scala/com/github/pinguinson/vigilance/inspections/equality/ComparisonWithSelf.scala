package com.github.pinguinson.vigilance.inspections.equality

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object ComparisonWithSelf extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Comparision with self"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(Select(left, TermName("$eq$eq")), List(right)) if left.toString == right.toString =>
          context.warn(tree.pos, self, "Comparing an object with itself doesn't make sense")
      }
    }
  }
}