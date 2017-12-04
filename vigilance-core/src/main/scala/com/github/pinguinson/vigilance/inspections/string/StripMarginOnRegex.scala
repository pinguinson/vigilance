package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class StripMarginOnRegex extends Inspection {

  override val level = Levels.Error
  override val description = "Strip margin on regex"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val R = TermName("r")
      private val StripMargin = TermName("stripMargin")
      private val Augment = TermName("augmentString")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(Apply(_, List(Select(Apply(Select(_, Augment), List(Literal(Constant(str: String)))), StripMargin))), R) if str.contains('|') =>
            context.warn(tree.pos, StripMarginOnRegex.this, "Strip margin will strip | from regex - possible corrupted regex")
          case _ => continue(tree)
        }
      }
    }
  }
}