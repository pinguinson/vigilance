package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class AvoidSizeEqualsZero extends Inspection {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def postTyperTraverser = Some apply new context.Traverser {

      import context.global._

      private val Size = TermName("size")
      private val Length = TermName("length")

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(Select(q, Size | Length), TermName("$eq$eq")), List(Literal(Constant(0)))) if q.tpe.<:<(typeOf[Traversable[_]]) =>
            context.warn("Avoid Traversable.size == 0",
              tree.pos,
              Levels.Warning,
              "Traversable.size is slow for some implementations. Prefer .isEmpty which is O(1): " + tree.toString().take(100),
              AvoidSizeEqualsZero.this)
          case _ => continue(tree)
        }
      }
    }
  }
}
