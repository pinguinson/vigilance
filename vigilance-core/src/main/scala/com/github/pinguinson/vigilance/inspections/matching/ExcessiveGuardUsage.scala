package com.github.pinguinson.vigilance.inspections.matching

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

object ExcessiveGuardUsage extends Inspection {

  override val level = Levels.Info
  override val description = "Excessive guard usage"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      def isExcessive(a: Apply, symbol: Name) = {
        a.toString.contains(s"${symbol.decode}.==") || a.toString.contains(s".==(${symbol.decode})")
      }

      override protected def inspect(tree: Tree) = {
        case CaseDef(b: Bind, a: Apply, _) if isExcessive(a, b.symbol.name) =>
          warn(tree.pos, self, "Pattern can be simplified")
      }

    }
  }

}
