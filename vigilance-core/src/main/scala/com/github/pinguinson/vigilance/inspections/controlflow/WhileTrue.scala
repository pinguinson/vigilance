package com.github.pinguinson.vigilance.inspections.controlflow

import com.github.pinguinson.vigilance._

/**
  * @author Stephen Samuel
  */
object WhileTrue extends Inspection {

  override val level = Levels.Warning
  override val description = "While true loop"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case LabelDef(name, _, If(Literal(Constant(true)), _, _)) if isWhile(name) =>
          context.warn(tree.pos, self, "A while true loop is unlikely to be meant for production")
        case LabelDef(name, _, Block(_, If(Literal(Constant(true)), _, _))) if isWhile(name) =>
          context.warn(tree.pos, self, "A do while true loop is unlikely to be meant for production")
      }

      private def isWhile(name: TermName): Boolean = {
        name.toString.startsWith("while$") || name.toString.startsWith("doWhile$")
      }
    }
  }
}