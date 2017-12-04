package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/**
 * @author Sanjiv Sahayam
 *
 * Inspired by Intellij inspection that does:
 *   myMap.get(key).getOrElse(defaultValue) –> myMap.getOrElse(key, defaultValue)
 */
class MapGetAndGetOrElse extends Inspection {

  override val level = Levels.Info
  override val description = "Use of .get.getOrElse instead of .getOrElse"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private def isMap(tree: Tree): Boolean = tree.tpe <:< typeOf[scala.collection.MapLike[_, _, _]]

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(TypeApply(Select(Apply(Select(left, TermName("get")), List(key)),
            TermName("getOrElse")), _), List(defaultValue)) if isMap(left) =>
            context.warn(
              tree.pos,
              MapGetAndGetOrElse.this,
              s"Use of .get($key).getOrElse($defaultValue) instead of getOrElse($key, $defaultValue): " + tree.toString.take(500)
            )
          case _ => continue(tree)
        }
      }
    }
  }
}
