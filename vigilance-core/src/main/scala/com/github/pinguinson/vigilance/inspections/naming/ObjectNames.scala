package com.github.pinguinson.vigilance.inspections.naming

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object ObjectNames extends Inspection { self =>

  override val level = Levels.Style
  override val description = "Object name not recommended"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val regex = "^[A-Za-z0-9]*$"

      override def inspect(tree: Tree) = {
        case ModuleDef(mods, name, _) if !mods.isSynthetic && !name.toString.matches(regex) =>
          context.warn(tree.pos, self, s"Object names should only contain alphanumeric chars (bad = $name)")
      }
    }
  }
}
