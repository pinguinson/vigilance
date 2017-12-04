package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class DuplicateSetValue extends Inspection {

  override val level = Levels.Warning
  override val description = "Duplicated set value"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private def hasDuplicates(trees: List[Tree]): Boolean = {
        val values: Set[Any] = trees.map {
          case Literal(Constant(x)) => x
          case x                    => x
        }.toSet
        values.size < trees.size
      }

      private def warn(tree: Tree) = {
        context.warn(tree.pos, DuplicateSetValue.this, "A set value is overwriten by a later entry: " + tree.toString().take(100))
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(TypeApply(Select(Select(_, TermName("Set")), TermName("apply")), _), args) if hasDuplicates(args) => warn(tree)
          case _ => continue(tree)
        }
      }
    }
  }
}