package com.github.pinguinson.vigilance.inspections.inference

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object PointlessTypeBounds extends Inspection {

  override val level = Levels.Warning
  override val description = "Pointless type bounds"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case TypeDef(_, _, _, rhs) if rhs.tpe.bounds.isEmptyBounds
          && rhs.pos != null
          && (rhs.pos.lineContent.contains("<: Any") || rhs.pos.lineContent.contains(">: Nothing")) =>
          context.warn(
            tree.pos,
            self,
            "Type bound resolves to Nothing <: T <: Any. Did you mean to put in other bounds: " + tree.toString.take(300)
          )
      }
    }
  }
}
