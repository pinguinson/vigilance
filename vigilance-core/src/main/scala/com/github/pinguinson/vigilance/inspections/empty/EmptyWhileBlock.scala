package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object EmptyWhileBlock extends Inspection {

  override val level = Levels.Warning
  override val description = "Empty while block"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case LabelDef(_, _, If(_, Block(List(Unit), _), _)) =>
          context.warn(tree.pos, self, tree.toString.take(500))
      }

    }
  }
}