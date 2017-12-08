package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

object ReverseFunc extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Unnecessary reverse"

  object FuncReplace {

    private val funcReplace = Map(
      "head" -> "last",
      "headOption" -> "lastOption",
      "iterator" -> "reverseIterator",
      "map" -> "reverseMap")

    def unapply(func: String): Option[(String, String)] =
      funcReplace.find(_._1 == func)
  }

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Select(SelectTraversable(Reverse), TermName(FuncReplace(func, replace))) =>
          warn(func, replace, tree)
        case Select(Apply(arrayOps1, List(Select(Apply(arrayOps2, List(_)), Reverse))), TermName(FuncReplace(func, replace)))
          if arrayOps1.toString.contains("ArrayOps") && arrayOps2.toString.contains("ArrayOps") =>
          warn(func, replace, tree)
      }

      private def warn(func: String, replace: String, tree: Tree): Unit =
        context.warn(
          tree.pos,
          self,
          s".reverse.$func can be replaced with $replace"
        )

    }
  }
}