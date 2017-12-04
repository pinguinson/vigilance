package com.github.pinguinson.vigilance.inspections.style

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}
import com.github.pinguinson.vigilance.inspections.style.AvoidOperatorOverload._

import scala.reflect.internal.Flags

/**
 * @author Stephen Samuel
 *
 *
 *         http://docs.scala-lang.org/style/naming-conventions.html#symbolic-method-names
 */
class AvoidOperatorOverload extends Inspection {

  override val level = Levels.Info
  override val description = "Avoid operator overload"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case DefDef(mods, _, _, _, _, _) if mods.hasFlag(Flags.SetterFlags) | mods.hasFlag(Flags.GetterFlags) =>
          case DefDef(_, nme.CONSTRUCTOR, _, _, _, _) =>
          case DefDef(_, TermName("$init$"), _, _, _, _) =>
          case DefDef(_, name, _, _, _, _) if name.toChars.count(_ == '$') > 2 =>
            context.warn(
              tree.pos,
              AvoidOperatorOverload.this,
              message(name.decode)
            )
          case _ => continue(tree)
        }
      }
    }
  }
}

object AvoidOperatorOverload {
  private val docsLink = "http://docs.scala-lang.org/style/naming-conventions.html#symbolic-method-names"
  private val message: String => String = names =>
    s"Scala style guide advocates against routinely using operators as method names ($names). $docsLink"
}
