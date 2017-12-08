package com.github.pinguinson.vigilance.inspections.nulls

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object NullParameter extends Inspection { self =>

  override val level = Levels.Warning
  override val description = "Null parameter"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case Apply(_, _) if tree.tpe.toString == "scala.xml.Elem" =>
        case Apply(_, args) if args.contains(Null) =>
          context.warn(tree.pos, self, "Null is used as a method parameter: " + tree.toString.take(300)) //TODO: improve comment
        case DefDef(mods, _, _, _, _, _) if mods.hasFlag(Flag.SYNTHETIC) =>
      }
    }
  }
}