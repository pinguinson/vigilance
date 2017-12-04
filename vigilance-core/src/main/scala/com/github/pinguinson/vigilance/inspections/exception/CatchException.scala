package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Marconi Lanna */
class CatchException extends Inspection {

  override val level = Levels.Warning
  override val description = "Catching Exception"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      def findExceptionCase(cases: List[CaseDef]) = {
        cases.find {
          // matches t : Exception
          case CaseDef(Bind(_, Typed(_, tpt)), _, _) if tpt.tpe =:= typeOf[Exception] => true
          // matches _ : Exception
          case CaseDef(Typed(_, tpt), _, _) if tpt.tpe =:= typeOf[Exception] => true
          case _ => false
        }
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Try(_, cases, _) =>
            findExceptionCase(cases).foreach { found =>
              context.warn(found.pos, CatchException.this, "Consider catching a more specific exception class")
            }
          case _ => continue(tree)
        }
      }
    }
  }
}
