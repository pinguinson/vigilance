package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/**
 * @author Stephen Samuel
 *
 *         Inspired by Intellij
 */
object ExistsSimplifiableToContains extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Exists simplifiable to contains"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def isTraversable(tree: Tree) = tree.tpe <:< typeOf[Traversable[_]]

      private def isContainsType(container: Tree, value: Tree): Boolean = {
        val valueType = value.tpe.underlying.typeSymbol.tpe
        val traversableType = container.tpe.underlying.baseType(typeOf[Traversable[Any]].typeSymbol)
        traversableType.typeArgs.exists(t => valueType <:< t || valueType =:= t)
      }

      override def inspect(tree: Tree) = {
        case Apply(Select(lhs, Exists), List(Function(_, Apply(Select(_, Equals), List(x))))) if isTraversable(lhs) && isContainsType(lhs, x) =>
          context.warn(tree.pos, self, "exists(x => x == y) can be replaced with contains(y)")
      }
    }
  }
}
