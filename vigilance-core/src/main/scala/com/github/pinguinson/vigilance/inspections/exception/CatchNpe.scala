package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class CatchNpe extends Inspection {

  override val level = Levels.Warning
  override val description = "Catching NPE"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private def findNpeCatch(cases: List[CaseDef]): Option[CaseDef] = {
        cases.find(_.pat.tpe =:= typeOf[NullPointerException])
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Try(_, catches, _) =>
            findNpeCatch(catches).foreach { found =>
              context.warn(found.pos, CatchNpe.this, "Catching NPE")
            }
          case _ => continue(tree)
        }
      }
    }
  }
}
