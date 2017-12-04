package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
class EmptyWhileBlock extends Inspection {

  override val level = Levels.Warning
  override val description = "Empty while block"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case LabelDef(_, _, If(_, Block(List(Literal(Constant(()))), _), _)) =>
            context.warn(tree.pos, EmptyWhileBlock.this, tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}