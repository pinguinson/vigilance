package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

import scala.collection.mutable

/** @author Stephen Samuel */
class UnreachableCatch extends Inspection {

  override val level = Levels.Warning
  override val description = "Unreachable catch"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

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

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Try(_, cases, _) =>
            getUnreachable(cases).foreach { found =>
              context.warn(found.pos, UnreachableCatch.this, "This case is unreachable")
            }
          case _ => continue(tree)
        }
      }
    }
  }
}

