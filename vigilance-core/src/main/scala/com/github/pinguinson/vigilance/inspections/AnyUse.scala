package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

import scala.reflect.internal.Flags

/** @author Stephen Samuel */
object AnyUse extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Use of Any"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      def warn(tree: Tree): Unit = {
        context.warn(tree.pos, self, "Use of Any should be avoided")
      }

      override def inspect(tree: Tree) = {
        case DefDef(mods, _, _, _, _, _) if mods.isSynthetic =>
        case DefDef(mods, _, _, _, _, _) if mods.hasFlag(Flags.SetterFlags) =>
        case DefDef(mods, _, _, _, _, _) if mods.hasFlag(Flags.GetterFlags) =>
        case ValDef(_, _, tpt, _) if tpt.tpe =:= typeOf[Any] => warn(tree)
        case DefDef(_, _, _, _, tpt, _) if tpt.tpe =:= typeOf[Any] => warn(tree)
      }
    }
  }
}

