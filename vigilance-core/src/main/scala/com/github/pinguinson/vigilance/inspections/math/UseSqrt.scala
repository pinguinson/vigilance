package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

class UseSqrt extends Inspection { self =>

  override val level = Levels.Info
  override val description = "Use math.sqrt"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {

     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def isMathPackage(tree: Tree): Boolean = {
        tree.symbol.fullNameString == "scala.math.package" ||
          tree.symbol.fullNameString == "java.lang.StrictMath" ||
          tree.symbol.fullNameString == "java.lang.Math"
      }

      override def inspect(tree: Tree) = {
        case Apply(Select(pack, TermName("pow")), List(_, Literal(Constant(0.5d)))) if isMathPackage(pack) =>
          val math = pack.toString().stripPrefix("java.lang.").stripPrefix("scala.").stripSuffix(".`package`")
          context.warn(tree.pos, self, s"$math.sqrt is clearer and more performant than $math.pow(x, 0.5)")
      }
    }
  }
}
