package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

class FinalModifierOnCaseClass extends Inspection {

  override val level = Levels.Info
  override val description = "Missing final modifier on case class"

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {

    import context.global._

    override def traverser = new context.Traverser {

      override def inspect(tree: Tree): Unit = {
        tree match {
          case ClassDef(mods, _, _, _) if !mods.hasAbstractFlag && mods.isCase && !mods.isFinal =>
            context.warn(tree.pos, FinalModifierOnCaseClass.this, "Case classes should have final modifier")
          case _ => continue(tree)
        }
      }
    }
  }
}
