package com.github.pinguinson.vigilance.inspections.unneccesary

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object UnnecessaryReturnUse extends Inspection {

  override val level = Levels.Error
  override val description = "Unnecessary return"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Block(_, Return(_)) => context.warn(tree.pos, self, tree.toString.take(500))
      }
    }
  }
}