package com.github.pinguinson.vigilance.inspections.naming

import com.github.pinguinson.vigilance._

import scala.concurrent.Future

/** @author Nikita Gusak */
object FutureOptionNames extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Future[Option[T]] method name"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case DefDef(_, name, _, _, tpt, _) if tpt.tpe <:< typeOf[Future[Option[Any]]] && !name.startsWith("find") =>
          context.warn(
            tree.pos,
            self,
            "Method returning Future[Option[T]] should have name prefixed with 'find'"
          )
      }
    }
  }
}
