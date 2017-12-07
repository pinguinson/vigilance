package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
class EmptyCaseClass extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Empty case class"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      def accessors(trees: List[Tree]): List[ValDef] = {
        trees.collect {
          case v: ValDef => v
        }.filter(_.mods.isCaseAccessor)
      }

      override def inspect(tree: Tree) = {
        // body should have constructor only, and with synthetic methods it has 10 in total
        case ClassDef(mods, _, Nil, Template(_, _, body)) if mods.isCase && accessors(body).isEmpty =>
          context.warn(tree.pos, self, "Empty case class can be rewritten as a case object")
      }
    }
  }
}

