package com.github.pinguinson.vigilance.inspections.unsafe

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class IsInstanceOf extends Inspection {

  override val level = Levels.Warning
  override val description = "Use of isInstanceOf"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case TypeApply(Select(_, TermName("isInstanceOf")), _) =>
            context.warn(
              tree.pos,
              IsInstanceOf.this,
              "Consider using a pattern match rather than isInstanceOf: " + tree.toString.take(500)
            )
          case DefDef(modifiers, _, _, _, _, _) if modifiers.hasFlag(Flag.SYNTHETIC) => // avoid partial function stuff
          case m @ Match(selector, cases) => // ignore selector and process cases
            cases.foreach(traverse)
          case _ => continue(tree)
        }
      }
    }
  }
}