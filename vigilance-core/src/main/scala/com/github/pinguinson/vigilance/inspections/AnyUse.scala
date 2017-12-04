package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

import scala.reflect.internal.Flags

/** @author Stephen Samuel */
class AnyUse extends Inspection {

  override val level = Levels.Info
  override val description = "Use of Any"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      def warn(tree: Tree): Unit = {
        context.warn(
          tree.pos,
          AnyUse.this,
          "Use of Any should be avoided: " + tree.toString.take(200)
        )
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case DefDef(mods, _, _, _, _, _) if mods.isSynthetic =>
          case DefDef(mods, _, _, _, _, _) if mods.hasFlag(Flags.SetterFlags) =>
          case DefDef(mods, _, _, _, _, _) if mods.hasFlag(Flags.GetterFlags) =>
          case ValDef(_, _, tpt, _) if tpt.tpe =:= typeOf[Any] => warn(tree)
          case DefDef(_, _, _, _, tpt, _) if tpt.tpe =:= typeOf[Any] => warn(tree)
          case _ => continue(tree)
        }
      }
    }
  }
}

