package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

/** @author Stephen Samuel */
object PublicFinalizer extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Public finalizer"

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {

    import context.global._

    override def traverser = new context.Traverser {

      override def inspect(tree: Tree) = {
        case DefDef(mods, TermName("finalize"), Nil, Nil, tpt, _) if mods.isPublic && tpt.tpe <:< typeOf[Unit] =>
          context.warn(
            tree.pos,
            self,
            "Public finalizer should be avoided as finalizers should not be programmatically invoked")
      }
    }
  }
}
