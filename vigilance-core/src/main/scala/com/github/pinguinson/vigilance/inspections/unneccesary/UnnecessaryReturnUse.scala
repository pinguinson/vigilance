package com.github.pinguinson.vigilance.inspections.unneccesary

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class UnnecessaryReturnUse extends Inspection {

  override val level = Levels.Info
  override val description = "Unnecessary return"
  //TODO: set level to Error, reference tpolecat blog post

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Block(_, Return(_)) => context.warn(tree.pos, UnnecessaryReturnUse.this, tree.toString.take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}