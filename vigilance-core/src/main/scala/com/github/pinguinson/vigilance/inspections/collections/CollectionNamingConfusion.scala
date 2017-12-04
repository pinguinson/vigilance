package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */

@deprecated("High chance of false positive", "0.0.2")
class CollectionNamingConfusion extends Inspection {

  override val level = Levels.Info
  override val description = "A Set is named list"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      private def isNamedSet(name: String): Boolean = name.trim == "set" || name.trim.contains("Set")
      private def isNamedList(name: String): Boolean = name.trim == "list" || name.trim.contains("List")
      private def isSet(tpe: Type) = tpe <:< typeOf[scala.collection.mutable.Set[_]] || tpe <:< typeOf[scala.collection.immutable.Set[_]]
      private def isList(tpe: Type) = tpe <:< typeOf[scala.collection.immutable.List[_]]

      override def inspect(tree: Tree): Unit = {
        tree match {
          case ValDef(_, TermName(name), tpt, _) if isSet(tpt.tpe) && isNamedList(name) =>
            context.warn(tree.pos, CollectionNamingConfusion.this, "An instanceof Set is confusingly referred to by a variable called/containing list: " +
                            tree.toString().take(300))
          case ValDef(_, TermName(name), tpt, _) if isList(tpt.tpe) && isNamedSet(name) =>
            context.warn(tree.pos, CollectionNamingConfusion.this, "An instanceof List is confusingly referred to by a variable called/containing set: " +
                            tree.toString().take(300))
          case _ => continue(tree)
        }
      }
    }
  }
}