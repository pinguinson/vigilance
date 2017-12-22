package com.github.pinguinson.vigilance.inspections.imports

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object WildcardImport extends Inspection {

  override val level = Levels.Warning
  override val description = "Wildcard import"

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {

    import context.global._

    private def isWildcard(trees: List[ImportSelector]): Boolean = trees.exists(_.name == nme.WILDCARD)

    override def traverser = new context.Traverser {

      override def inspect(tree: Tree) = {
        case Import(_, selector) if isWildcard(selector) =>
          context.warn(tree.pos, self, "Wildcard import used: " + tree.toString)
      }
    }
  }
}
