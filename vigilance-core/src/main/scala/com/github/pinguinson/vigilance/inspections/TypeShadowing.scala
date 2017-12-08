package com.github.pinguinson.vigilance.inspections

import scala.collection.mutable

import com.github.pinguinson.vigilance.{Levels, Inspection, InspectionContext, Inspector}

/** @author Stephen Samuel */
object TypeShadowing extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Type shadowing"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def checkShadowing(tparams: List[TypeDef], trees: List[Tree]): Unit = {
        val types = mutable.HashSet[String]()
        tparams.foreach(tparam => types.add(tparam.name.toString))
        trees.foreach {
          case dd: DefDef if dd.symbol != null && dd.symbol.isSynthetic =>
          case dd@DefDef(_, name, deftparams, _, _, _) =>
            deftparams.foreach(tparam => {
              if (types.contains(tparam.name.toString))
                warn(dd, name, tparam)
            })
          case ClassDef(_, _, tparams2, Template(_, _, body)) => checkShadowing(tparams2, body)
          case _ =>
        }
      }

      private def warn(dd: DefDef, name: TermName, tparam: TypeDef) {
        context.warn(dd.pos, self, s"Method $name declares shadowed type parameter ${tparam.name}")
      }

      override def inspect(tree: Tree) = {
        case ClassDef(_, _, tparams, Template(_, _, body)) => checkShadowing(tparams, body)
      }
    }
  }
}

