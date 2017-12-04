package com.github.pinguinson.vigilance.inspections.imports

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class WildcardImport extends Inspection {

  override val level = Levels.Warning
  override val description = "Wildcard import"

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {

    import context.global._

    private def isWildcard(trees: List[ImportSelector]): Boolean = trees.exists(_.name == nme.WILDCARD)

    override def traverser = new context.Traverser {

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Import(expr, selector) if isWildcard(selector) =>
            context.warn(tree.pos, WildcardImport.this, "Wildcard import used: " + tree.toString())
          case _ => continue(tree)
        }
      }
    }
  }
}
