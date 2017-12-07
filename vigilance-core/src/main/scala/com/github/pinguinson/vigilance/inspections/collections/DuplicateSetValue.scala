package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class DuplicateSetValue extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Duplicated set value"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def hasDuplicates(trees: List[Tree]): Boolean = {
        val values: Set[Any] = trees.map {
          case Literal(Constant(x)) => x
          case x                    => x
        }.toSet
        values.size < trees.size
      }

      //TODO: rework as DuplicateMapKey
      override def inspect(tree: Tree) = {
        case Apply(TypeApply(Select(Select(_, TermName("Set")), TermName("apply")), _), args) if hasDuplicates(args) =>
          context.warn(tree.pos, self, "A set value is overwritten by a later entry: " + tree.toString().take(100))
      }
    }
  }
}
