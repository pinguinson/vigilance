package com.github.pinguinson.vigilance.inspections.nulls

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
class NullParameter extends Inspection {

  override val level = Levels.Warning
  override val description = "Null parameter"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      def containsNull(trees: List[Tree]) = trees exists {
        case Literal(Constant(null)) => true
        case _                       => false
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(_, _) if tree.tpe.toString == "scala.xml.Elem" =>
          case Apply(_, args) =>
            if (containsNull(args))
              warn(tree)
          case DefDef(mods, _, _, _, _, _) if mods.hasFlag(Flag.SYNTHETIC) =>
          case _ => continue(tree)
        }
      }
      private def warn(tree: Tree) {
        context.warn(tree.pos, NullParameter.this, "Null is used as a method parameter: " + tree.toString.take(300))
      }
    }
  }
}