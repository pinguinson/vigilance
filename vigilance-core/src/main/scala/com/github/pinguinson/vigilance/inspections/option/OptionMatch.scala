package com.github.pinguinson.vigilance.inspections.option

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

object OptionMatch extends Inspection {

  override val level = Levels.Info
  override val description = "Excessive pattern match usage with Option"

  override def inspector(context: InspectionContext) = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def isSomeToSome(caseDef: CaseDef): Boolean = {
        caseDef.pat.tpe <:< typeOf[Some[Any]] && caseDef.body.tpe <:< typeOf[Some[Any]]
      }

      private def isNoneToNone(caseDef: CaseDef): Boolean = {
        caseDef.pat.tpe <:< typeOf[None.type] && caseDef.body.tpe <:< typeOf[None.type]
      }

      override protected def inspect(tree: Tree) = {
        case Match(lhs, cases) if lhs.tpe <:< typeOf[Option[Any]] =>
          if (cases.forall(c => isSomeToSome(c) || isNoneToNone(c)))
            context.warn(tree.pos, self, "Can be replaced with Option.map")
      }
    }
  }
}
