package com.github.pinguinson.vigilance.inspections.naming

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class ClassNames extends Inspection {

  override val level = Levels.Info
  override val description = "Class name not recommended"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val regex = "^[A-Z][A-Za-z0-9]*$"

      override def inspect(tree: Tree): Unit = {
        tree match {
          case ClassDef(_, name, _, _) if name.toString.contains("$anon") =>
          case ClassDef(mods, name, _, _) if !mods.isSynthetic && !name.toString.matches(regex) =>
            context.warn(tree.pos, ClassNames.this, s"Class names should begin with uppercase letter (bad = $name)")
          case _ => continue(tree)
        }
      }
    }
  }
}
