package com.github.pinguinson.vigilance.inspections.style

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

import scala.reflect.internal.Flags

/**
  * @author Stephen Samuel
  *
  *
  *         http://docs.scala-lang.org/style/naming-conventions.html#symbolic-method-names
  */
object AvoidOperatorOverload extends Inspection {

  override val level = Levels.Info
  override val description = "Avoid operator overload"

  private val docsLink = "http://docs.scala-lang.org/style/naming-conventions.html#symbolic-method-names"
  private val message: String => String = names => s"Scala style guide advocates against routinely using operators as method names ($names). $docsLink"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {

        case DefDef(mods, _, _, _, _, _) if mods.hasFlag(Flags.SetterFlags) | mods.hasFlag(Flags.GetterFlags) =>
        case DefDef(_, nme.CONSTRUCTOR, _, _, _, _) =>
        case DefDef(_, TermName("$init$"), _, _, _, _) =>
        case DefDef(_, name, _, _, _, _) if name.toChars.count(_ == '$') > 2 =>
          context.warn(
            tree.pos,
            self,
            message(name.decode)
          )
      }
    }
  }
}