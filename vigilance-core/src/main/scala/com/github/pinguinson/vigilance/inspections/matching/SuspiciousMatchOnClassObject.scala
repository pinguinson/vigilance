package com.github.pinguinson.vigilance.inspections.matching

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class SuspiciousMatchOnClassObject extends Inspection {

  override val level = Levels.Warning
  override val description = "Suspicious match on class object"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Match(selector, cases) =>
            checkCases(cases)
            continue(tree)
          case _ => continue(tree)
        }
      }

      private def checkCases(cases: List[CaseDef]): Unit = {
        cases.exists {
          case c @ CaseDef(pat, _, _) // if we have a case object and a companion class, then we are matching on an object instead of a class
          if pat.symbol != null &&
            pat.symbol.isModuleOrModuleClass &&
            pat.tpe.typeSymbol.companionClass.isClass &&
            !pat.tpe.typeSymbol.companionClass.isAbstractClass =>
            warn(c)
            true
          case _ => false
        }
      }

      private def warn(tree: Tree) {
        context.warn(tree.pos, SuspiciousMatchOnClassObject.this, tree.toString.take(500))
      }
    }
  }
}