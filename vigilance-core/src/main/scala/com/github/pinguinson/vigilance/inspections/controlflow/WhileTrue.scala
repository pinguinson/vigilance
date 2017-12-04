package com.github.pinguinson.vigilance.inspections.controlflow

import com.github.pinguinson.vigilance._

/**
 * @author Stephen Samuel
 */
class WhileTrue extends Inspection {

  override val level = Levels.Warning
  override val description = "While true loop"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        this.getClass.getSimpleName
        tree match {
          case LabelDef(name, _, If(cond, _, _)) if isWhile(name) && isConstantCondition(cond) =>
            context.warn(tree.pos, WhileTrue.this, "A while true loop is unlikely to be meant for production: " + tree.toString.take(500))
          case LabelDef(name, _, Block(_, If(cond, _, _))) if isWhile(name) && isConstantCondition(cond) =>
            context.warn(tree.pos, WhileTrue.this, "A do while true loop is unlikely to be meant for production: " + tree.toString.take(500))
          case _ => continue(tree)
        }
      }

      private def isConstantCondition(tree: Tree): Boolean = tree match {
        case Literal(Constant(true)) => true
        case _                       => false
      }

      private def isWhile(name: TermName): Boolean = {
        name.toString.startsWith("while$") || name.toString.startsWith("doWhile$")
      }
    }
  }
}