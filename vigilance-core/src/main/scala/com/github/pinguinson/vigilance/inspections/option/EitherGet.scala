package com.github.pinguinson.vigilance.inspections.option

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object EitherGet extends Inspection { self =>

  override val level = Levels.Error
  override val description = "Use of Either Projection get"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Select(SelectEither(Left | Right), Get) =>
          context.warn(tree.pos, self, tree.toString.take(500))
      }
    }
  }
}