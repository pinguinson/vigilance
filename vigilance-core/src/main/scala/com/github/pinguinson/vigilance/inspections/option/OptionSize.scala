package com.github.pinguinson.vigilance.inspections.option

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class OptionSize extends Inspection {

  override val level = Levels.Info
  override val description = "Prefer Option.isDefined instead of Option.size"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(Apply(option2Iterable, List(opt)), TermName("size")) ⇒
            if (option2Iterable.symbol.fullName == "scala.Option.option2Iterable")
              context.warn(tree.pos, OptionSize.this, tree.toString().take(500))
          case _ ⇒ continue(tree)
        }
      }
    }
  }
}