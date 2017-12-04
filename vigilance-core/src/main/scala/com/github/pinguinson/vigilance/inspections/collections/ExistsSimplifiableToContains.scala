package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/**
 * @author Stephen Samuel
 *
 *         Inspired by Intellij
 */
class ExistsSimplifiableToContains extends Inspection {

  override val level = Levels.Info
  override val description = "Exists simplifiable to contains"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Equals = TermName("$eq$eq")

      private def isTraversable(tree: Tree) = tree.tpe <:< typeOf[Traversable[Any]]

      private def isContainsType(container: Tree, value: Tree): Boolean = {
        val valueType = value.tpe.underlying.typeSymbol.tpe
        val traversableType = container.tpe.underlying.baseType(typeOf[Traversable[Any]].typeSymbol)
        traversableType.typeArgs.exists(t => valueType <:< t || valueType =:= t)
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(lhs, TermName("exists")), List(Function(_, Apply(Select(_, Equals), List(x))))) if isTraversable(lhs) && isContainsType(lhs, x) =>
            context.warn(tree.pos, ExistsSimplifiableToContains.this, "exists(x => x == y) can be replaced with contains(y): " + tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}
