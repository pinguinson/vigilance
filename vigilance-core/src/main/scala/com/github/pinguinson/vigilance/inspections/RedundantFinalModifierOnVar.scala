package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

class RedundantFinalModifierOnVar extends Inspection {

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {

    import context.global._

    override def postTyperTraverser = Some apply new context.Traverser {

      override def inspect(tree: Tree): Unit = {
        tree match {
          case ValDef(mods, _, _, _) if mods.isFinal && mods.isMutable &&
            (tree.symbol.enclClass.isFinal ||
              tree.symbol.enclClass.isCase ||
              tree.symbol.enclClass.isModuleOrModuleClass ||
              tree.symbol.enclClass.isPackageObjectOrClass) =>
            context.warn("Redundant final modifier on var",
              tree.pos,
              Levels.Info,
              "This var cannot be overridden, final modifier is redundant",
              RedundantFinalModifierOnVar.this)
          case _ => continue(tree)
        }
      }
    }
  }
}
