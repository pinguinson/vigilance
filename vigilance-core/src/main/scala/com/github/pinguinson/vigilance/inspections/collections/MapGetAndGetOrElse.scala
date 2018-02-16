package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/**
 * @author Sanjiv Sahayam
 *
 * Inspired by Intellij inspection that does:
 *   myMap.get(key).getOrElse(defaultValue) –> myMap.getOrElse(key, defaultValue)
 */
object MapGetAndGetOrElse extends Inspection {

  override val level = Levels.Info
  override val description = "Use of .get.getOrElse"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def isMap(tree: Tree): Boolean = tree.tpe <:< typeOf[scala.collection.MapLike[_, _, _]]

      override def inspect(tree: Tree) = {
        case Apply(TypeApply(Select(Apply(Select(left, TermName("get")), List(key)), TermName("getOrElse")), _), List(default)) if isMap(left) =>
          context.warn(tree.pos, self, s"Use getOrElse($key, $default) instead of .get($key).getOrElse($default)")
      }
    }
  }
}
