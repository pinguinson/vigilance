package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

class FindAndNotEqualsNoneReplaceWithExists extends Inspection {

  override val level = Levels.Info
  override val description = "filter(_.isDefined).map(_.get)"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(Apply(Select(_, TermName("find")), _), TermName("$bang$eq")), List(Select(_, TermName("None")))) =>
            context.warn(tree.pos, FindAndNotEqualsNoneReplaceWithExists.this, ".filter(_.isDefined).map(_.get) can be replaced with flatten: " + tree.toString.take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}