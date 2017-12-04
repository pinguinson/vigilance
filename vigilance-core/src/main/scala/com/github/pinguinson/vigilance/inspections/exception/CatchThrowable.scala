package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class CatchThrowable extends Inspection {

  override val level = Levels.Warning
  override val description = "Catching Throwable"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      def findThrowableCatch(cases: List[CaseDef]): Option[CaseDef] = {
        cases.find {
          // matches t : Throwable
          case CaseDef(Bind(_, Typed(_, tpt)), _, _) if tpt.tpe =:= typeOf[Throwable] => true
          // matches _ : Throwable
          case CaseDef(Typed(_, tpt), _, _) if tpt.tpe =:= typeOf[Throwable] => true
          case _ => false
        }
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Try(_, catches, _) =>
            findThrowableCatch(catches).foreach { found =>
              context.warn(
                found.pos,
                CatchThrowable.this,
                "Consider catching a more specific exception class"
              )
            }
          case _ => continue(tree)
        }
      }
    }
  }
}

