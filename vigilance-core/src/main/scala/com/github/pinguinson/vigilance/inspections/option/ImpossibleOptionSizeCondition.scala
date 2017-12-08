package com.github.pinguinson.vigilance.inspections.option

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object ImpossibleOptionSizeCondition extends Inspection { self =>

  override val level = Levels.Error
  override val description = "Option.size is either 0 or 1"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val Opt2Iterable = TermName("option2Iterable")
      private val Size = TermName("size")
      private val Greater = TermName("$greater")

      override def inspect(tree: Tree) = {
        case Apply(Select(Select(Apply(TypeApply(Select(_, Opt2Iterable), _), _), Size), Greater),
        List(Literal(Constant(x: Int)))) if x > 1 =>
          context.warn(tree.pos, self, tree.toString.take(200))
      }
    }
  }
}

