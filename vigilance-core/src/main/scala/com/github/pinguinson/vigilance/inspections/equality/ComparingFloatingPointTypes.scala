package com.github.pinguinson.vigilance.inspections.equality

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class ComparingFloatingPointTypes extends Inspection {

  override val level = Levels.Error
  override val description = "Floating type comparison"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private val Equals = TermName("$eq$eq")
      private val NotEquals = TermName("$bang$eq")

      private def isFloating(tree: Tree) = {
        tree.tpe <:< typeOf[Double] || tree.tpe <:< typeOf[Float]
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(left, Equals | NotEquals), List(right)) if isFloating(left) && isFloating(right) =>
            context.warn(tree.pos, ComparingFloatingPointTypes.this, "You should not compare floating point number with == or !=")
          case _ => continue(tree)
        }
      }
    }
  }
}
