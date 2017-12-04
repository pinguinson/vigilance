package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

class UseLog1P extends Inspection {

  override val level = Levels.Info
  override val description = "Use math.log1p"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      def isMathPackage(pack: String) =
        (pack == "scala.math.package"
          || pack == "java.lang.Math"
          || pack == "java.lang.StrictMath")

      def warn(tree: Tree, math: String): Unit = {
        context.warn(tree.pos, UseLog1P.this, s"$math.log1p(x) is clearer and more performant than $math.log(1 + x)")
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(pack, TermName("log")), List(Apply(Select(Literal(Constant(1)), nme.ADD), _))) if isMathPackage(pack.symbol.fullName) =>
            val math = pack.toString().stripSuffix(".`package`").substring(pack.toString().lastIndexOf('.'))
            warn(tree, math)

          case Apply(Select(pack, TermName("log")), List(Apply(Select(_, nme.ADD), List(Literal(Constant(1)))))) if isMathPackage(pack.symbol.fullName) =>
            val math = pack.toString().stripSuffix(".`package`").substring(pack.toString().lastIndexOf('.'))
            warn(tree, math)

          case _ => continue(tree)
        }
      }
    }
  }
}
