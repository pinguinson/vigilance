package com.github.pinguinson.vigilance.inspections.option

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class OptionGet extends Inspection {

  override val level = Levels.Error
  override val description = "Use of Option.get"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
            case Select(lhs, TermName("get")) if lhs.tpe <:< typeOf[Option[_]] =>
              context.warn(
                tree.pos,
                OptionGet.this,
                tree.toString.take(500)
              )
          case _ => continue(tree)
        }
      }
    }
  }
}
