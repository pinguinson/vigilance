package com.github.pinguinson.vigilance.inspections.matching

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

import scala.collection.mutable

/** @author Stephen Samuel */
class RepeatedCaseBody extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Repeated case body"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      // FIXME: this one looks really dirty
      private def isRepeated(cases: List[CaseDef]): Boolean = {
        val filteredCases = cases.filter(caseDef => caseDef.guard == EmptyTree && caseDef.body.children.size > 4)
        val bodies = mutable.HashSet[String]()
        for (caseDef <- filteredCases) {
          bodies add caseDef.body.toString()
        }
        bodies.size < filteredCases.size
      }

      override def inspect(tree: Tree) = {
        case Match(_, cases) if isRepeated(cases) =>
          context.warn(tree.pos, self, "Case body is repeated. Consider merging pattern clauses together: " + tree.toString().take(500))
      }
    }
  }
}