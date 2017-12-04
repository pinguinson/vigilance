package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class BigDecimalDoubleConstructor extends Inspection {

  override val level = Levels.Warning
  override val description = "Big decimal double constructor"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._
      import definitions.{ DoubleClass, FloatClass }

      private def isBigDecimal(pack: Tree) =
        pack.toString == "scala.`package`.BigDecimal" || pack.toString == "java.math.BigDecimal"
      private def isFloatingPointType(tree: Tree) = tree.tpe <:< FloatClass.tpe || tree.tpe <:< DoubleClass.tpe

      private def warn(tree: Tree): Unit = {
        context.warn(tree.pos, BigDecimalDoubleConstructor.this, tree.toString().take(100))
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(pack, TermName("apply")), arg :: tail) if isBigDecimal(pack) && isFloatingPointType(arg) =>
            warn(tree)
          case Apply(Select(New(pack), nme.CONSTRUCTOR),
            arg :: tail) if isBigDecimal(pack) && isFloatingPointType(arg) =>
            warn(tree)
          case _ => continue(tree)
        }
      }
    }
  }
}