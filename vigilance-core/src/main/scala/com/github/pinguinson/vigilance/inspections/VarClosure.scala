package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Levels, Inspection, InspectionContext, Inspector}

/** @author Stephen Samuel */
object VarClosure extends Inspection {

  override val level = Levels.Warning
  override val description = "Var closure"

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {

    import context.global._

    override def traverser = new context.Traverser {

      private def capturesVar(tree: Tree): Unit = tree match {
        case Block(stmt, expr) => (stmt :+ expr).foreach(capturesVar)
        case Apply(Select(_, _), args) =>
          args
            .filter(_.symbol != null)
            .filter(_.symbol.isMethod)
            .filter(_.symbol.isGetter)
            .filterNot(_.symbol.isStable)
            .foreach { arg =>
              context.warn(tree.pos, self, s"Closing over a var can lead to subtle bugs ${arg.symbol.name.toString}")
            }
        case _ =>
      }

      override def inspect(tree: Tree) = {
        case Function(List(ValDef(_, _, _, _)), body) => capturesVar(body)
      }
    }

  }
}
