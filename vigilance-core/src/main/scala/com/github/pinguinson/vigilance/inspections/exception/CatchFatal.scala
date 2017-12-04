package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

import scala.util.control.ControlThrowable

/** @author Marconi Lanna */
class CatchFatal extends Inspection {

  override val level = Levels.Warning
  override val description = "Catching fatal exception"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      def isFatal(tpe: Type): Boolean = {
        tpe =:= typeOf[VirtualMachineError] ||
          tpe =:= typeOf[ThreadDeath] ||
          tpe =:= typeOf[InterruptedException] ||
          tpe =:= typeOf[LinkageError] ||
          tpe =:= typeOf[ControlThrowable]
      }

      def findFatalCatch(cases: List[CaseDef]): Option[CaseDef] = {
        cases.find {
          // matches t : FatalException
          case CaseDef(Bind(_, Typed(_, tpt)), _, _) if isFatal(tpt.tpe) => true
          // matches _ : FatalException
          case CaseDef(Typed(_, tpt), _, _) if isFatal(tpt.tpe) => true
          case _ => false
        }
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Try(_, cases, _) =>
            findFatalCatch(cases).foreach { found =>
              context.warn(found.pos, CatchFatal.this, "Catching fatal exceptions is discouraged")
            }
          case _ => continue(tree)
        }
      }
    }
  }
}
