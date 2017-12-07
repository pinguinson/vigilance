package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class ListAppend extends Inspection { self =>

  override val level = Levels.Info
  override val description = "List append is slow"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(TypeApply(SelectList(Append), _), _) =>
          context.warn(
            tree.pos,
            self,
            "List append is O(n). For large lists, consider using cons (::) or another data structure such as ListBuffer or Vector and converting to a List once built."
          )
      }
    }
  }
}