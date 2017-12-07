package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Stephen Samuel */
class ArrayEquals extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Array equals"

  override def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def isArray(tree: Tree) = tree match {
        // "null" is deprecated, but comparing an array to null can be useful
        // in some cases (e.g. when interfacing with null-using Java code) and
        // shouldn't trigger this warning, which relates to whether the
        // equality is reference-equality or value-equality.
        //
        // We have to add a special case here, because the 'type' of null will
        // be coerced to Array when it is compared to an array, so it would
        // otherwise match the next case.
        case Literal(Constant(null)) => false
        case x                       => x.tpe <:< typeOf[Array[_]]
      }

      override def inspect(tree: Tree) = {
        case Apply(Select(lhs, Equals | NotEquals), List(rhs)) if isArray(lhs) && isArray(rhs) =>
          context.warn(tree.pos, self, "Array equals is not an equality check. Use a.deep == b.deep or convert to another collection type")
      }
    }
  }
}