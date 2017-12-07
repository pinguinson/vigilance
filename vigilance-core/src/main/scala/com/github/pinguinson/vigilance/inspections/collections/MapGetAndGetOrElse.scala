package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/**
 * @author Sanjiv Sahayam
 *
 * Inspired by Intellij inspection that does:
 *   myMap.get(key).getOrElse(defaultValue) –> myMap.getOrElse(key, defaultValue)
 */
class MapGetAndGetOrElse extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Use of .get.getOrElse"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(TypeApply(Select(Apply(SelectMap(Get), _), GetOrElse), _), _) =>
          context.warn(
            tree.pos,
            self,
            s"Use getOrElse(x, y) instead of .get(x).getOrElse(y)"
          )
      }
    }
  }
}
