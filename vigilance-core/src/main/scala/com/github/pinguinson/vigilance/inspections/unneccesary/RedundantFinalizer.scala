package com.github.pinguinson.vigilance.inspections.unneccesary

import com.github.pinguinson.vigilance._

/**
  * @author Stephen Samuel
  *
  *         Inspired by http://findbugs.sourceforge.net/bugDescriptions.html#FI_USELESS
  */
object RedundantFinalizer extends Inspection {

  override val level = Levels.Warning
  override val description = "Redundant finalizer"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {

        case DefDef(mods, name, _, _, tpt, _) if mods.hasFlag(Flag.OVERRIDE) && name.toString == "finalize" && tpt.toString == "Unit" =>
          context.warn(tree.pos, self, tree.toString.take(500))
      }
    }
  }
}
