package com.github.pinguinson.vigilance.inspections.string

import java.util.IllegalFormatException

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class IllegalFormatString extends Inspection { self =>

  override val level = Levels.Error
  override val description = "Illegal format string"

  // format is: %[argument_index$][flags][width][.precision][t]conversion
  final val argRegex = "%(\\d+\\$)?[-#+ 0,(\\<]*?\\d?(\\.\\d+)?[tT]?[a-zA-Z]".r

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(Select(Apply(Select(_, TermName("augmentString")), List(Literal(Constant(format)))),
        TermName("format")), _) =>
          val argCount = argRegex.findAllIn(format.toString).matchData.size
          val args = Nil.padTo(argCount, null)
          try {
            String.format(format.toString, args: _*)
          } catch {
            case e: IllegalFormatException =>
              println(e)
              context.warn(tree.pos, self, "A format string contains an illegal syntax: " + e.getMessage)
          }
      }
    }
  }
}
