package com.github.pinguinson.vigilance.inspections.unneccesary

import com.github.pinguinson.vigilance._

/**
 * @author Stephen Samuel
 *
 *         Checks for if statements where the condition evalutes to a constant true or a constant false.
 *
 */
class ConstantIf extends Inspection {

  override val level = Levels.Warning
  override val description = "Constant if expression"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          // ignore while loops, this will be picked up by the WhileTrue inspection
          case LabelDef(_, _, _) =>
          case If(cond, thenp, elsep) =>
            if (cond.toString() == "false" || cond.toString() == "true")
              context.warn(tree.pos, ConstantIf.this, "Constant if expression " + tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}
