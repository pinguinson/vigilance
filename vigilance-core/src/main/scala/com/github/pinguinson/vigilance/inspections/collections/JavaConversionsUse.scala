package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class JavaConversionsUse extends Inspection {

  override val level = Levels.Warning
  override val description = "Java conversions"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Import(expr, selectors) if expr.symbol.fullName == "scala.collection.JavaConversions" =>
            context.warn(tree.pos, JavaConversionsUse.this, "Use of java conversions can lead to unusual behaviour. It is recommended to use JavaConverters")
          case _ => continue(tree)
        }
      }
    }
  }
}