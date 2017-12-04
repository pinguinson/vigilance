package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class NoOpOverride extends Inspection {

  override val level = Levels.Info
  override val description = "Unnecessary override"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case DefDef(mods, name, _, vparamss, _, Apply(Select(Super(This(_), _), name2), args)) if name == name2 && vparamss.foldLeft(0)((a, b) => a + b.size) == args.size =>
            context.warn(tree.pos, NoOpOverride.this, "This method is overridden yet only calls super: " + tree.toString().take(200))
          case _ => continue(tree)
        }
      }
    }
  }
}