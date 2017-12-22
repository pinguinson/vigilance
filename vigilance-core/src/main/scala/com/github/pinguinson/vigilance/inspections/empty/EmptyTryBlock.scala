package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object EmptyTryBlock extends Inspection {

  override val level = Levels.Warning
  override val description = "Empty try block"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Try(Unit, _, _) =>
          context.warn(tree.pos, self, tree.toString.take(500))
      }
    }
  }
}