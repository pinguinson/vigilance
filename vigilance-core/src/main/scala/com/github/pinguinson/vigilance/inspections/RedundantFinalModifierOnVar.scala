package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

object RedundantFinalModifierOnVar extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Redundant final modifier on var"

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {

    import context.global._

    override def traverser = new context.Traverser {

      override def inspect(tree: Tree) = {

        case ValDef(mods, _, _, _) if mods.isFinal && mods.isMutable &&
          (tree.symbol.enclClass.isFinal ||
            tree.symbol.enclClass.isCase ||
            tree.symbol.enclClass.isModuleOrModuleClass ||
            tree.symbol.enclClass.isPackageObjectOrClass) =>
          context.warn(tree.pos, self, "This var cannot be overridden, final modifier is redundant")
      }
    }
  }
}
