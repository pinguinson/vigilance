package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object DuplicateMapKey extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Duplicated map key"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val Arrow = TermName("$minus$greater")
      private val UnicodeArrow = TermName("$u2192")

      private def findDuplicateKeys(trees: Seq[Tree]): Seq[String] = {
        val keys = trees.foldLeft(Seq.empty[String])((keys, tree) => tree match {
          case Apply(TypeApply(Select(Apply(_, args), Arrow | UnicodeArrow), _), _) =>
            keys :+ args.head.toString()
          case _ => keys
        })
        keys.groupBy(identity).collect {
          case (key, list) if list.size > 1 => key
        }.toSeq
      }

      override def inspect(tree: Tree) = {
        case Apply(TypeApply(Select(Select(_, Map), TermApply), _), args) =>
          val duplicates = findDuplicateKeys(args)
          if (duplicates.nonEmpty)
            context.warn(tree.pos, self, "Duplicate map keys found: " + duplicates.mkString(", "))
      }
    }
  }
}
