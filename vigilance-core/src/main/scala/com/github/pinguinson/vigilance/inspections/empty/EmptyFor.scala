package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
class EmptyFor extends Inspection {

  override val level = Levels.Warning
  override val description = "Empty for loop"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Foreach = TermName("foreach")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(TypeApply(Select(_, Foreach), _), List(Function(List(ValDef(_, _, _, EmptyTree)), Literal(Constant(()))))) =>
            context.warn(tree.pos, EmptyFor.this, tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}

