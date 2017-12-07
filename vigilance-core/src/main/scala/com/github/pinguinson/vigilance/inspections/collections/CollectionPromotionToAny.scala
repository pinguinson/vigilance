package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/**
  * @author Stephen Samuel
  *         This inspection was inspired by http://p5wscala.wordpress.com/scalaprocessing-gotchas/#t2
  */
class CollectionPromotionToAny extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Collection promotion to any"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      //TODO: this is soooo dirty, fix it
      private def isSeq(symbol: Symbol): Boolean = {
        val full = symbol.typeSignature.resultType.typeSymbol.fullName
        full.startsWith("scala.collection.immutable") &&
          (full.endsWith("List") || full.endsWith("Set") || full.endsWith("Seq") || full.endsWith("Vector"))
      }

      private def isAny(tree: Tree): Boolean = tree.toString() == "Any"

      private def isAny(symbol: Symbol): Boolean = symbol.typeSignature.resultType.typeArgs.headOption match {
        case Some(t) => t.toString == "Any"
        case None => false
      }

      private def isAnySeq(tree: Tree): Boolean = tree match {
        case select@Select(_, _) if select.symbol != null => isSeq(select.symbol) && isAny(select.symbol)
        case _ => false
      }

      override def inspect(tree: Tree) = {
        case TypeApply(Select(l, TermName("$colon$plus")), List(a, r)) =>
          if (!isAnySeq(l) && isAny(a))
            context.warn(
              tree.pos,
              self,
              tree.toString.take(100)
            )
      }
    }
  }
}
