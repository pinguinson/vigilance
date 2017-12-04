package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class FilterOptionAndGet extends Inspection {

  override val level = Levels.Info
  override val description = "filter(_.isDefined).map(_.get)"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(TypeApply(
            Select(Apply(Select(_, TermName("filter")), List(Function(_, Select(_, TermName("isDefined"))))),
              TermName("map")), args), List(Function(_, Select(_, TermName("get"))))) =>
            context.warn(tree.pos, FilterOptionAndGet.this, ".filter(_.isDefined).map(_.get) can be replaced with flatten: " + tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}