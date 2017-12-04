package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class EmptyTryBlock extends Inspection {

  override val level = Levels.Warning
  override val description = "Empty try block"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Try(Literal(Constant(())), _, _) =>
            context.warn(tree.pos, EmptyTryBlock.this, tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}