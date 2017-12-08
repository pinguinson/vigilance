package com.github.pinguinson.vigilance.inspections.equality

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object ComparingFloatingPointTypes extends Inspection { self =>

  override val level = Levels.Error
  override val description = "Floating type comparison"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      //
      override def inspect(tree: Tree) = {
        case Apply(Select(TreeFloating(_), Equals | NotEquals), List(TreeFloating(_))) =>
          context.warn(tree.pos, self, "You should not compare floating point number with == or !=")
      }
    }
  }
}
