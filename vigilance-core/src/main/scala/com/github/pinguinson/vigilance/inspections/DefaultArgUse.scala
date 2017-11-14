package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector }

/** @author Stephen Samuel */
class DefaultArgUse extends Inspection {
  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def postTyperTraverser = Some apply new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case _ => continue(tree)
        }
      }
    }
  }
}
