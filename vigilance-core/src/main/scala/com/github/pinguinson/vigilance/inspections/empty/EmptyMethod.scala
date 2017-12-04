package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class EmptyMethod extends Inspection {

  override val level = Levels.Warning
  override val description = "Empty method"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          // its ok to do empty impl for overridden methods
          case DefDef(mods, _, _, _, _, _) if mods.isOverride =>
          case ClassDef(mods, _, _, _) if mods.isTrait => continue(tree)
          case DefDef(_, _, _, _, _, _) if tree.symbol != null && tree.symbol.enclClass.isTrait =>
          case DefDef(mods, _, _, _, _, Literal(Constant(()))) =>
            context.warn(tree.pos, EmptyMethod.this, "Empty method statement " + tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}
