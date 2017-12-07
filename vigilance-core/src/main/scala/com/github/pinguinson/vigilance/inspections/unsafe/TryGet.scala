package com.github.pinguinson.vigilance.inspections.unsafe

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class TryGet extends Inspection { self =>

  override val level = Levels.Error
  override val description = "Use of Try.get"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case SelectTry(Get) => context.warn(tree.pos, self, "Try.get is unsafe and thereby discouraged")
      }
    }
  }
}
