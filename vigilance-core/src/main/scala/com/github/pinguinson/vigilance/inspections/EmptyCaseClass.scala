package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class EmptyCaseClass extends Inspection {

  override val level = Levels.Info
  override val description = "Empty case class"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      def accessors(trees: List[Tree]): List[ValDef] = {
        trees.collect {
          case v: ValDef => v
        }.filter(_.mods.isCaseAccessor)
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          // body should have constructor only, and with synthetic methods it has 10 in total
          case ClassDef(mods, _, List(), Template(_, _, body)) if mods.isCase && accessors(body).isEmpty =>
            context.warn(tree.pos, EmptyCaseClass.this, "Empty case class can be rewritten as a case object")
          case _ => continue(tree)
        }
      }
    }
  }
}
