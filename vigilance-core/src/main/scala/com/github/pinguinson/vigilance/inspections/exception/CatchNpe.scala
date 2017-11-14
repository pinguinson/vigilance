package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class CatchNpe extends Inspection {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def postTyperTraverser = Some apply new context.Traverser {

      import context.global._

      private def catchesNpe(cases: List[CaseDef]): Boolean = {
        cases.exists(_.pat.tpe.toString == "NullPointerException")
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Try(block, catches, finalizer) if catchesNpe(catches) =>
            context.warn("Catching NPE", tree.pos, level = Levels.Error, CatchNpe.this)
          case _ => continue(tree)
        }
      }
    }
  }
}
