package com.github.pinguinson.vigilance.inspections.matching

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object PartialFunctionInsteadOfMatch extends Inspection {

  override val level = Levels.Info
  override val description = "Match instead of partial function"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def warn(tree: Tree) {
        context.warn(
          tree.pos,
          self,
          "A map match can be replaced with a partial function for greater readability: " + tree.toString.take(500)
        )
      }

      private def isPFBind(name: TermName) = {
        val b = name.toString.matches("x0\\$\\d+")
        b
      }

      override def inspect(tree: Tree): PartialFunction[Tree, Unit] = {
        // _ match { case ...; case ... }
        // need to not warn on the partial function style, they use x0$1
        case Apply(_, List(Function(List(ValDef(mods, name1, _, EmptyTree)), Match(name2, _)))) if name1.toString == name2.toString =>
          if (!isPFBind(name1)) warn(tree)
        case Apply(TypeApply(_, _), List(Function(List(ValDef(mods, name1, _, EmptyTree)), Match(name2, _)))) if name1.toString == name2.toString =>
          if (!isPFBind(name1)) warn(tree)
        case TypeApply(_, List(Function(List(ValDef(mods, name1, _, EmptyTree)), Match(name2, _)))) if name1.toString == name2.toString =>
          if (!isPFBind(name1)) warn(tree)
        // a => a match { case ...; case ... }
        //          case Apply(_, List(Function(List(ValDef(mods, x1, TypeTree(), EmptyTree)), Match(x2, _))))
        //            if x1.toString == x2.toString =>
        //            warn(tree)
      }
    }
  }
}
