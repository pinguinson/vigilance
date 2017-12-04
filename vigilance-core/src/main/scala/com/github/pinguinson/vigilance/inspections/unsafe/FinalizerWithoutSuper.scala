package com.github.pinguinson.vigilance.inspections.unsafe

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class FinalizerWithoutSuper extends Inspection {

  override val level = Levels.Warning
  override val description = "Finalizer without super"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Finalize = TermName("finalize")
      private def containsSuper(tree: Tree): Boolean = tree match {
        case Apply(Select(Super(_, _), Finalize), List()) => true
        case Block(stmts, expr) => (stmts :+ expr).exists(containsSuper)
        case _ => false
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case dd @ DefDef(mods, Finalize, _, _, tpt, rhs) if tpt.tpe <:< typeOf[Unit] =>
            if (!containsSuper(rhs))
              context.warn(tree.pos, FinalizerWithoutSuper.this, "Finalizers should call super.finalize() to ensure superclasses are able to run any finalization logic")
          case _ => continue(tree)
        }
      }
    }
  }
}