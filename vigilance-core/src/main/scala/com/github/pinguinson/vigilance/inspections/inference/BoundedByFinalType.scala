package com.github.pinguinson.vigilance.inspections.inference

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class BoundedByFinalType extends Inspection {

  override val level = Levels.Warning
  override val description = "Bounded by final type"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._
      import definitions._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case dd @ DefDef(mods, _, _, _, _, _) if dd.symbol != null && dd.symbol.owner.tpe.baseClasses.contains(PartialFunctionClass) =>
          case tdef: TypeDef if tdef.symbol.isAliasType =>
          case TypeDef(_, _, _, typeTree: TypeTree) =>
            typeTree.original match {
              case TypeBoundsTree(lo, hi) if lo.tpe.isFinalType && hi.tpe.isFinalType =>
                context.warn(
                  tree.pos,
                  BoundedByFinalType.this,
                  "Pointless type bound. Type parameter can only be a single value: " + tree.toString.take(300)
                )
              case _ =>
            }
          case _ => continue(tree)
        }
      }
    }
  }
}