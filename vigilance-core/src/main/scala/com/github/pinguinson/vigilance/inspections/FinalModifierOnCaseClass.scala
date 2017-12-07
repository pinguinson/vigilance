package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

class FinalModifierOnCaseClass extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Missing final modifier on case class"

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {

    import context.global._

    override def traverser = new context.Traverser {

      override def inspect(tree: Tree) = {
        case ClassDef(mods, _, _, _) if !mods.hasAbstractFlag && mods.isCase && !mods.isFinal =>
          context.warn(tree.pos, self, "Case classes should have final modifier")
      }
    }
  }
}
