package com.github.pinguinson.vigilance.inspections.imports

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

import scala.collection.mutable

/** @author Stephen Samuel */
object DuplicateImport extends Inspection {

  override val level = Levels.Info
  override val description = "Duplicated import"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {

    private val imports = mutable.HashSet.empty[String]

     override def traverser = new context.Traverser {
       import context._
       import context.global._

       override def inspect(tree: Tree) = {
         case PackageDef(_, _) =>
           imports.clear(); continue(tree)
         case ModuleDef(_, _, _) =>
           imports.clear(); continue(tree)
         case ClassDef(_, _, _, _) =>
           imports.clear(); continue(tree)
         case Import(expr, selectors) =>
           selectors.foreach(selector => {
             val name = expr.toString + "." + selector.name
             if (imports.contains(name)) {
               context.warn(tree.pos, self, name)
             }
             imports.add(name)
           })
         case DefDef(_, _, _, _, _, _) => // check imports inside defs
       }
    }
  }
}


