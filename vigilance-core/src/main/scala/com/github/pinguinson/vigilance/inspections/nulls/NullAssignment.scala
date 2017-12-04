package com.github.pinguinson.vigilance.inspections.nulls

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class NullAssignment extends Inspection {

  override val level = Levels.Warning
  override val description = "Null assignment"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Null = Literal(Constant(null))

      override def inspect(tree: Tree): Unit = {
        tree match {
          case ValDef(_, _, _, Null) =>
            warn(tree)
          case Apply(Select(_, name), List(Null)) if name.endsWith("_$eq") =>
            warn(tree)
          case Assign(_, Null) =>
            warn(tree)
          case _ => continue(tree)
        }
      }

      private def warn(tree: Tree) {
        context.warn(tree.pos, NullAssignment.this, "Null assignment on line " + tree.pos.line)
      }
    }
  }
}