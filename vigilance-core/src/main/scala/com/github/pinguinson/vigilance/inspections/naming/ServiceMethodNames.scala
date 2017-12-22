package com.github.pinguinson.vigilance.inspections.naming

import com.github.pinguinson.vigilance._

import scala.concurrent.Future

/** @author Nikita Gusak */
object ServiceMethodNames extends Inspection {

  override val level = Levels.Style
  override val description = "Service method names"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def inspectChildren(tree: Tree): Unit = tree match {
        case DefDef(_, name, _, _, tpt, _) =>
          if (tpt.tpe <:< typeOf[Future[Option[Any]]] && !name.startsWith("find"))
            context.warn(tree.pos, self, "Method returning Future[Option[T]] should have name prefixed with 'find'")
          if (tpt.tpe <:< typeOf[Future[Seq[Any]]] && !name.startsWith("list"))
            context.warn(tree.pos, self, "Method returning Future[Seq[T]] should have name prefixed with 'list'")
        case _ =>
      }

      override def inspect(tree: Tree) = {
        case ClassDef(mods, name, _, Template(_, _, body)) if mods.isTrait && name.endsWith("Service") => body.foreach(inspectChildren)
      }
    }
  }
}
