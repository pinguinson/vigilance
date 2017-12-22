package com.github.pinguinson.vigilance.inspections.unsafe

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object FinalizerWithoutSuper extends Inspection {

  override val level = Levels.Warning
  override val description = "Finalizer without super"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def containsSuper(tree: Tree): Boolean = tree match {
        case Apply(Select(Super(_, _), Finalize), List()) => true
        case Block(statements, expr) => (statements :+ expr).exists(containsSuper)
        case _ => false
      }

      override def inspect(tree: Tree) = {
        case DefDef(_, Finalize, _, _, tpt, rhs) if tpt.tpe <:< typeOf[Unit] && !containsSuper(rhs) =>
          context.warn(tree.pos, self, "Finalizers should call super.finalize() to ensure superclasses are able to run any finalization logic")
      }
    }
  }
}

