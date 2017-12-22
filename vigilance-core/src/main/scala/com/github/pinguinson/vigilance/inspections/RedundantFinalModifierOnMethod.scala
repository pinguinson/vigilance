package com.github.pinguinson.vigilance.inspections

import scala.reflect.internal.Flags

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

object RedundantFinalModifierOnMethod extends Inspection {

  override val level = Levels.Info
  override val description = "Redundant final modifier on method"

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {

    import context.global._
    import definitions._

    override def traverser = new context.Traverser {

      override def inspect(tree: Tree) = {

        case DefDef(mods, _, _, _, _, _) if tree.symbol != null && tree.symbol.owner.tpe.baseClasses.contains(PartialFunctionClass) =>
        case dd: DefDef if dd.symbol != null && dd.symbol.isSynthetic =>
        case DefDef(mods, _, _, _, _, _) if mods.hasFlag(Flags.ACCESSOR) =>
        case DefDef(_, nme.CONSTRUCTOR, _, _, _, _) =>
        case dd@DefDef(mods, name, _, _, _, _) if mods.isFinal &&
          (tree.symbol.enclClass.isFinal ||
            tree.symbol.enclClass.isCase ||
            tree.symbol.enclClass.isModuleOrModuleClass ||
            tree.symbol.enclClass.isPackageObjectOrClass) =>
          context.warn(
            tree.pos,
            self,
            s"${dd.symbol.fullName} cannot be overridden, final modifier is redundant")
      }
    }
  }
}
