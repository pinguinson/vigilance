package com.github.pinguinson.vigilance.inspections.naming

import com.github.pinguinson.vigilance._

import scala.concurrent.Future

/** @author Nikita Gusak */
class FutureSeqNames extends Inspection {

  override val level = Levels.Info
  override val description = "Future[Seq[T]] method name"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case DefDef(_, name, _, _, tpt, _) if tpt.tpe <:< typeOf[Future[Seq[_]]] && !name.startsWith("list") =>
            context.warn(
              tree.pos,
              FutureSeqNames.this,
              "Method returning Future[Seq[T]] should have name prefixed with 'list'"
            )
          case _ => continue(tree)
        }
      }
    }
  }
}
