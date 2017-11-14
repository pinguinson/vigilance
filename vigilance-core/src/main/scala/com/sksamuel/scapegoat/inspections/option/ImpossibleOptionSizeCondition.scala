package com.github.pinguinson.vigilance.inspections.option

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class ImpossibleOptionSizeCondition
    extends Inspection {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def postTyperTraverser = Some apply new context.Traverser {

      import context.global._

      private val Opt2Iterable = TermName("option2Iterable")
      private val Size = TermName("size")
      private val Greater = TermName("$greater")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(Select(Apply(TypeApply(Select(_, Opt2Iterable), _), _), Size), Greater),
            List(Literal(Constant(x: Int)))) if x > 1 =>
            context.warn("Option.size > " + x + " can never be true",
              tree.pos, Levels.Error,
              tree.toString().take(200),
              ImpossibleOptionSizeCondition.this)
          case _ => continue(tree)
        }
      }
    }
  }

}
