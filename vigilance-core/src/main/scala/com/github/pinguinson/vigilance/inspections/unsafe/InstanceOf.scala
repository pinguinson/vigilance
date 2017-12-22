package com.github.pinguinson.vigilance.inspections.unsafe

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object InstanceOf extends Inspection {

  override val level = Levels.Warning
  override val description = "Use of InstanceOf"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      // asInstanceOf[scala.xml.Elem] is fine, i guess
      private def isXml(tree: Tree) = {
        tree.tpe.toLongString == "scala.xml.Elem"
      }

      override def inspect(tree: Tree) = {
        case TypeApply(Select(lhs, IsInstanceOf | AsInstanceOf), _) if lhs.toString != "classOf[java.lang.Class]" && !isXml(tree) =>
          context.warn(tree.pos, self, "Consider using a pattern match rather than asInstanceOf/isInstanceOf")
        case Match(_, cases) => cases.foreach(traverse)
      }
    }
  }
}