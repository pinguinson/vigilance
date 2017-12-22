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
          args.filter(_.symbol != null)
            .foreach(arg => if (arg.symbol.isMethod && arg.symbol.isGetter && !arg.symbol.isStable) {
              context.warn(tree.pos, self, "Closing over a var can lead to subtle bugs: " + tree.toString.take(500))
            })
        case _ =>
      }

      override def inspect(tree: Tree) = {
        case Function(List(ValDef(_, _, _, _)), body) => capturesVar(body)
      }
    }

  }
}
