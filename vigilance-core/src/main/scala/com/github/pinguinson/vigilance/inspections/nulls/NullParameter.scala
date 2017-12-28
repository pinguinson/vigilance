package com.github.pinguinson.vigilance.inspections.nulls

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object NullParameter extends Inspection {

  override val level = Levels.Warning
  override val description = "Null parameter"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def containsNull(trees: List[Tree]) = trees exists {
        case Literal(Constant(null)) => true
        case _                       => false
      }

      override def inspect(tree: Tree) = {
        case Apply(_, args) if tree.tpe.toString != "scala.xml.Elem" && containsNull(args) =>
          context.warn(tree.pos, self, "Null is used as a method parameter")
      }
    }
  }
}