package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class SubstringZero extends Inspection {

  override val level = Levels.Info
  override val description = "String.substring(0)"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Substring = TermName("substring")
      private val StringType = typeOf[String]

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(lhs, Substring), List(Literal(Constant(0)))) if lhs.tpe <:< StringType =>
            context.warn(tree.pos, SubstringZero.this, "Use of String.substring(0) will always return the same string: " + tree.toString().take(100))
          case _ => continue(tree)
        }
      }
    }
  }
}
