package com.github.pinguinson.vigilance.inspections.unsafe

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class InstanceOf extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Use of isInstanceOf"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case TypeApply(Select(lhs, IsInstanceOf | AsInstanceOf), _) if lhs.toString != "classOf[java.lang.Class]" =>
          context.warn(tree.pos, self, "Consider using a pattern match rather than asInstanceOf/isInstanceOf")
        case Match(_, cases) => cases.foreach(traverse)
      }
    }
  }
}