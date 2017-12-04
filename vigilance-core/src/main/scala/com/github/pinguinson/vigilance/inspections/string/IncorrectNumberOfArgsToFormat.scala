package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class IncorrectNumberOfArgsToFormat extends Inspection {

  override val level = Levels.Error
  override val description = "Incorrect number of args for format"

  // format is: %[argument_index$][flags][width][.precision][t]conversion
  final val argRegex = "%(\\d+\\$)?[-#+ 0,(\\<]*?\\d?(\\.\\d+)?[tT]?[a-zA-Z]".r

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(Apply(Select(_, TermName("augmentString")), List(Literal(Constant(format)))),
            TermName("format")), args) =>
            val argCount = argRegex.findAllIn(format.toString).matchData.size
            if (argCount > args.size)
              context.warn(tree.pos, IncorrectNumberOfArgsToFormat.this, tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}
