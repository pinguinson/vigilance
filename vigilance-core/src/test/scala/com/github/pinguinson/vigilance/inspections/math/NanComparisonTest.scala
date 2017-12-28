package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

class NanComparisonTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(NanComparison)

  "nan comparison" - {
    "should report warning" - {
      val code =
        """class Test {
          |
          |  val d = 0.5d
          |  d == Double.NaN
          |  Double.NaN == d
          |
          |  val f = 0.5f
          |  f == Double.NaN
          |  Double.NaN == f
          |
          |} """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 4
    }
  }
}
