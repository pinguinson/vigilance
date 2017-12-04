package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class SwallowedException extends Inspection {

  override val level = Levels.Warning
  override val description = "Empty catch block"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Unit = Literal(Constant(()))

      private def checkCatches(cases: List[CaseDef]): Unit = cases.foreach {
        case CaseDef(Bind(TermName("ignored") | TermName("ignore"), _), _, _) =>
        case catchBlock @ CaseDef(_, _, Unit) =>
          context.warn(catchBlock.pos, SwallowedException.this, "Empty catch block")
        case _ =>
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Try(_, catches, _) => checkCatches(catches)
          case _                  => continue(tree)
        }
      }
    }
  }
}