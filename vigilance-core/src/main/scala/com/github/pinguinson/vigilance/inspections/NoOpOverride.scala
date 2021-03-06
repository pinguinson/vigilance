package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object NoOpOverride extends Inspection {

  override val level = Levels.Info
  override val description = "Unnecessary override"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case DefDef(_, name, _, vparamss, _, Apply(Select(Super(This(_), _), name2), args)) if name == name2 && vparamss.map(_.size).sum == args.size =>
          context.warn(tree.pos, self, "This method is overridden yet only calls super")
      }
    }
  }
}
