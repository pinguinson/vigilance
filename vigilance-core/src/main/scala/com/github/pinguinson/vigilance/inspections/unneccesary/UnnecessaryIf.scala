package com.github.pinguinson.vigilance.inspections.unneccesary

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class UnnecessaryIf extends Inspection {

  override val level = Levels.Info
  override val description = "Unnecessary if condition"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case If(cond, Literal(Constant(true)), Literal(Constant(false))) =>
            context.warn(tree.pos, UnnecessaryIf.this, tree.toString.take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}