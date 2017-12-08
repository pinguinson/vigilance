package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object EmptyFor extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Empty for loop"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val Foreach = TermName("foreach")

      override def inspect(tree: Tree) = {
        case Apply(TypeApply(Select(_, Foreach), _), List(Function(List(ValDef(_, _, _, EmptyTree)), Literal(Constant(()))))) =>
          context.warn(tree.pos, self, tree.toString().take(500))
      }
    }
  }
}

