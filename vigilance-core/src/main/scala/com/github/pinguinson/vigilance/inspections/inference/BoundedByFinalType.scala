package com.github.pinguinson.vigilance.inspections.inference

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
class BoundedByFinalType extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Bounded by final type"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case typeDef @ TypeDef(_, _, _, typeTree: TypeTree) if !typeDef.symbol.isAliasType =>
          typeTree.original match {
            case TypeBoundsTree(lo, hi) if lo.tpe.isFinalType && hi.tpe.isFinalType =>
              context.warn(
                tree.pos,
                self,
                "Pointless type bound. Type parameter can only be a single value: " + tree.toString.take(300)
              )
            case _ =>
          }
      }
    }
  }
}