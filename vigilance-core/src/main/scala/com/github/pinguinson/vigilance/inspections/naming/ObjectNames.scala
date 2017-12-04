package com.github.pinguinson.vigilance.inspections.naming

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class ObjectNames extends Inspection {

  override val level = Levels.Info
  override val description = "Object name not recommended"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val regex = "^[A-Za-z0-9]*$"

      override def inspect(tree: Tree): Unit = {
        tree match {
          case ModuleDef(mods, name, _) if !mods.isSynthetic && !name.toString.matches(regex) =>
            context.warn(tree.pos, ObjectNames.this, s"Object names should only contain alphanum chars (bad = $name)")
          case _ => continue(tree)
        }
      }
    }
  }
}