package com.github.pinguinson.vigilance.inspections.option

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class EitherGet extends Inspection {

  override val level = Levels.Error
  override val description = "Use of Either Projection get"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(Select(lhs, TermName("right") | TermName("left")), TermName("get")) if lhs.tpe <:< typeOf[Either[_, _]] =>
            context.warn(tree.pos, EitherGet.this, tree.toString.take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}