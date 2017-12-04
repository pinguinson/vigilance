package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/**
 * @author Stephen Samuel
 *
 *         Inspired by IntelliJ
 */
class FilterSize extends Inspection {

  override val level = Levels.Info
  override val description = "filter().size() instead of count()"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(Apply(Select(_, TermName("filter")), _), TermName("size")) =>
            context.warn(tree.pos, FilterSize.this, ".filter(x => Bool).size can be replaced with count(x => Bool): " + tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}
