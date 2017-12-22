package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object JavaConversionsUse extends Inspection {

  override val level = Levels.Warning
  override val description = "Use of Java conversions"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Import(expr, _) if expr.symbol.fullName == "scala.collection.JavaConversions" =>
          context.warn(tree.pos, self, "Use of java conversions can lead to unusual behaviour. It is recommended to use JavaConverters instead")
      }
    }
  }
}