package com.github.pinguinson.vigilance.inspections.style

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class ParameterlessMethodReturnsUnit extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Parameterless methods returns unit"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case DefDef(_, _, _, params, tpt, _) if tpt.tpe =:= typeOf[Unit] && params.isEmpty =>
          context.warn(tree.pos, self, tree.toString.take(300))
      }
    }
  }
}
