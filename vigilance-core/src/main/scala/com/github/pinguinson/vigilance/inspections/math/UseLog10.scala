package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

object UseLog10 extends Inspection {

  override val level = Levels.Info
  override val description = "Use math.log10"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def isMathPackage(pack: String) =
        (pack == "scala.math.package"
          || pack == "java.lang.Math"
          || pack == "java.lang.StrictMath")

      override def inspect(tree: Tree) = {
        case Apply(Select(Apply(Select(pack1, TermName("log")), _), nme.DIV), List(Apply(Select(pack2, TermName("log")), List(Literal(Constant(10.0))))))
          if isMathPackage(pack1.symbol.fullName) && isMathPackage(pack2.symbol.fullName) =>
          val math = pack1.toString.stripSuffix(".package").substring(pack2.toString.lastIndexOf('.'))
          context.warn(
            tree.pos,
            self,
            s"$math.log10(x) is clearer and more performant than $math.log(x)/$math.log(10)"
          )
      }
    }
  }
}
