package com.github.pinguinson.vigilance.inspections.unneccesary

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object UnnecessaryIf extends Inspection {

  override val level = Levels.Info
  override val description = "Unnecessary if condition"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case If(_, True | False, False | True) =>
          context.warn(tree.pos, self, tree.toString.take(500))
      }
    }
  }
}