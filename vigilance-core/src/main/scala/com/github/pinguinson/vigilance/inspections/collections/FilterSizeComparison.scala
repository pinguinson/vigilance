package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/**
 * @author Stephen Samuel
 *
 *         Inspired by IntelliJ
 *
 *         Checks for filter.size > 0, filter.size == 0, etc
 */
class FilterSizeComparison extends Inspection {

  override val level = Levels.Info
  override val description = "filter().isEmpty instead of !exists()"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          // todo
          case Select(Apply(Select(_, TermName("filter")), _), TermName("isEmpty")) =>
            context.warn(tree.pos, FilterSizeComparison.this, ".filter(x => Bool).isEmpty can be replaced with !exists(x => Bool): " + tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}
