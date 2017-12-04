package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

class LooksLikeInterpolatedString extends Inspection {

  override val level = Levels.Warning
  override val description = "Looks Like Interpolated String"

  final val regex1 = "\\$\\{[a-z][.a-zA-Z0-9_]*\\}".r
  final val regex2 = "\\$[a-z][.a-zA-Z0-9_]*".r

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Select(qual@Ident(TermName("scala")), name@TermName("StringContext")) => continue(tree)
          case Literal(Constant(str: String)) =>
            val possibles1 = regex1.findAllIn(str).toList.filterNot(_.contains("$anonfun"))
            val possibles2 = regex2.findAllIn(str).toList.filterNot(_.contains("$anonfun"))
            if ((possibles1 ++ possibles2).nonEmpty) {
              context.warn(tree.pos, LooksLikeInterpolatedString.this, str)
            }
          case _ => continue(tree)
        }
      }
    }
  }
}
