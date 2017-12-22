package com.github.pinguinson.vigilance.inspections.option

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object OptionSize extends Inspection {

  override val level = Levels.Info
  override val description = "Prefer Option.isDefined instead of Option.size"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Select(Apply(lhs, _), Size) if lhs.symbol.fullName == "scala.Option.option2Iterable" ⇒
          context.warn(tree.pos, self, tree.toString.take(500))
        case _ ⇒ continue(tree)
      }
    }
  }
}