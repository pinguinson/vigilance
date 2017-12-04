package com.github.pinguinson.vigilance.inspections.equality

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class ComparisonWithSelf extends Inspection {

  override val level = Levels.Warning
  override val description = "Comparision with self"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(left, TermName("$eq$eq")), List(right)) =>
            if (left.toString() == right.toString())
              context.warn(tree.pos, ComparisonWithSelf.this, tree.toString.take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}