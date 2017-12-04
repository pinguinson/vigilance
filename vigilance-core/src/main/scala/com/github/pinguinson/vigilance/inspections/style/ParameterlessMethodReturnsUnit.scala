package com.github.pinguinson.vigilance.inspections.style

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class ParameterlessMethodReturnsUnit extends Inspection {

  override val level = Levels.Warning
  override val description = "Parameterless methods returns unit"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case DefDef(_, _, _, params, tpt, _) if tpt.tpe =:= typeOf[Unit] && params.isEmpty =>
            context.warn(tree.pos, ParameterlessMethodReturnsUnit.this, tree.toString.take(300))
          case _ => continue(tree)
        }
      }
    }
  }
}
