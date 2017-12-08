package com.github.pinguinson.vigilance.inspections.string

import java.util.regex.PatternSyntaxException

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object InvalidRegex extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Invalid regex"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Select(Apply(Select(_, TermName("augmentString")), List(Literal(Constant(regex)))), TermName("r")) =>
          try {
            regex.toString.r
          } catch {
            case e: PatternSyntaxException =>
              context.warn(tree.pos, self, e.getMessage)
          }
      }
    }
  }
}