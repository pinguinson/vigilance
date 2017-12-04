package com.github.pinguinson.vigilance.inspections.unsafe

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class TryGet extends Inspection {

  override val level = Levels.Error
  override val description = "Use of Try.get"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(left, TermName("get")) if left.tpe <:< typeOf[scala.util.Try[_]] =>
              context.warn(
                tree.pos,
                TryGet.this,
                tree.toString.take(500)
              )
          case _ => continue(tree)
        }
      }
    }
  }
}
