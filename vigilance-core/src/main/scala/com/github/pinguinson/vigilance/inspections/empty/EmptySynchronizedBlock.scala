package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class EmptySynchronizedBlock extends Inspection {

  override val level = Levels.Warning
  override val description = "Empty synchronized block"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Sync = TermName("synchronized")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(TypeApply(Select(_, Sync), _), List(Literal(Constant(())))) =>
            context.warn(tree.pos, EmptySynchronizedBlock.this, tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}