package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

import scala.collection.mutable
import scala.util.control.ControlThrowable

/** @author Marconi Lanna */
object CatchOperations extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Catching exceptions"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val isNpe: Tree => Boolean = tree =>
        tree.tpe =:= typeOf[NullPointerException]

      private val isException: Tree => Boolean = tree =>
        tree.tpe =:= typeOf[Exception]

      private val isThrowable: Tree => Boolean = tree =>
        tree.tpe =:= typeOf[Throwable]

      private val isFatal: Tree => Boolean = tree => {
        val fatalCases = Seq(
          typeOf[VirtualMachineError],
          typeOf[ThreadDeath],
          typeOf[InterruptedException],
          typeOf[LinkageError],
          typeOf[ControlThrowable]
        )
        fatalCases.exists(_ =:= tree.tpe)
      }

      private val checks = Seq(
        isException -> "Consider catching a more specific exception class",
        isFatal -> "Catching fatal exceptions is discouraged",
        isNpe -> "Catching NPE",
        isThrowable -> "Consider catching a more specific exception class"
      )

      def find(cases: List[CaseDef])(predicate: Tree => Boolean) = {
        cases.find {
          // matches t : Type
          case CaseDef(Bind(_, Typed(_, tpt)), _, _) if predicate(tpt) => true
          // matches _ : Type
          case CaseDef(Typed(_, tpt), _, _) if predicate(tpt) => true
          case _ => false
        }
      }

      def getUnreachable(cases: List[CaseDef]): Option[Tree] = {
        val trees = mutable.HashSet[Tree]()

        def check(tpt: Tree): Boolean = {
          if (trees.exists(tpt.tpe <:< _.tpe)) true
          else {
            trees.add(tpt)
            false
          }
        }

        cases.find {
          case CaseDef(Bind(_, Typed(_, tpt)), _, _) => check(tpt)
          case CaseDef(Typed(_, tpt), _, _) if tpt.tpe =:= typeOf[Throwable] => check(tpt)
          case _ => false
        }
      }

      override def inspect(tree: Tree) = {

        case Try(_, cases, _) =>
          getUnreachable(cases).foreach { found =>
            context.warn(found.pos, self, "This case is unreachable")
          }
          for {
            (check, comment) <- checks
            found <- find(cases)(check)
          } yield context.warn(found.pos, self, comment)
      }
    }
  }
}
