package com.github.pinguinson.vigilance.inspections.inference

import com.github.pinguinson.vigilance.{Levels, Inspection, InspectionContext, Inspector}

import scala.reflect.internal.Flags

/** @author Stephen Samuel */
object ProductWithSerializableInferred extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Product with Serializable inferred"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private val Product = typeOf[Product]
      private val Serializable = typeOf[Serializable]
      private val Obj = typeOf[Object]

      private def isProductWithSerializable(tpe: Type): Boolean = {
        tpe.typeArgs match {
          case List(RefinedType(List(Product, Serializable, Obj), decls)) => true
          case _ => false
        }
      }

      override def inspect(tree: Tree) = {

        case ValDef(mods, _, _, _) if mods.hasFlag(Flags.SYNTHETIC) =>
        case ValDef(_, _, tpt, _) if isProductWithSerializable(tpt.tpe) =>
          context.warn(tree.pos, self, "It is unlikely that this was your target type: " + tree.toString().take(300))
      }
    }
  }
}
