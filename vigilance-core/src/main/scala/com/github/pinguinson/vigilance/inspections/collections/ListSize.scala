package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class ListSize extends Inspection {

  override val level = Levels.Info
  override val description = "List.size is O(n)"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(lhs, TermName("size")) if lhs.tpe <:< typeOf[List[_]] =>
            context.warn(tree.pos, ListSize.this, "List.size is O(n). Consider using a different data type with O(1) size lookup such as Vector or Array.")
          case _ => continue(tree)
        }
      }
    }
  }
}
