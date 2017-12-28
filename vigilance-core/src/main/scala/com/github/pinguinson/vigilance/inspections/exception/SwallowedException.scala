package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object SwallowedException extends Inspection {

  override val level = Levels.Warning
  override val description = "Empty catch block"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def checkCatches(cases: List[CaseDef]): Unit = cases.foreach {
        case CaseDef(Bind(TermName("ignored") | TermName("ignore"), _), _, _) =>
        case catchBlock @ CaseDef(_, _, Literal(Constant(()))) =>
          context.warn(catchBlock.pos, self, "Empty catch block")
        case _ =>
      }

      override def inspect(tree: Tree) = {
        case Try(_, catches, _) => checkCatches(catches)
        case _ => continue(tree)
      }
    }
  }
}