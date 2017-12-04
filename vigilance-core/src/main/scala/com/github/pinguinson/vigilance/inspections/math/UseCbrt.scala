package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance._

/** @author Matic Potočnik */
class UseCbrt extends Inspection {

  override val level = Levels.Info
  override val description = "Use math.cbrt"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(pack, TermName("pow")), List(_, Literal(Constant(third: Double))))
            if (pack.symbol.fullNameString == "scala.math.package"
              || pack.symbol.fullNameString == "java.lang.Math"
              || pack.symbol.fullNameString == "java.lang.StrictMath")
            && third >= 0.3333332
            && third <= 0.3333334 =>
            val math = pack.symbol.fullNameString.stripSuffix(".package").substring(pack.symbol.fullNameString.lastIndexOf('.'))
            context.warn(
              tree.pos,
              UseCbrt.this,
              s"$math.cbrt is clearer and more performant than $math.pow(x, 1/3)"
            )
          case _ => continue(tree)
        }
      }
    }
  }
}
