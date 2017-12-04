package com.github.pinguinson.vigilance.inspections.inference

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

import scala.reflect.internal.Flags

/** @author Stephen Samuel */
class MethodReturningAny extends Inspection {

  override val level = Levels.Warning
  override val description = "Method returning Any"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case DefDef(mods, _, _, _, _, _) if mods.isSynthetic                =>
          case DefDef(mods, _, _, _, _, _) if mods.hasFlag(Flags.SetterFlags) =>
          case DefDef(mods, _, _, _, _, _) if mods.hasFlag(Flags.GetterFlags) =>
          case DefDef(mods, _, _, _, _, _) if mods.hasFlag(Flags.ACCESSOR)    =>
          /// ignore overridden methods as the parent will receive the warning
          case DefDef(mods, _, _, _, _, _) if mods.isOverride                 =>
          case DefDef(_, _, _, _, tpt, _) if tpt.tpe =:= typeOf[Any] || tpt.tpe =:= typeOf[AnyRef] =>
            context.warn(tree.pos, MethodReturningAny.this, "Method returns Any. Consider using a more specialized type: " + tree.toString.take(300))
          case _ => continue(tree)
        }
      }
    }
  }
}
