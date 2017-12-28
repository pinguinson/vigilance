package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class BigDecimalDoubleConstructorTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(BigDecimalDoubleConstructor)

  "big decimal double constructor" - {
    "should report warning" in {
      val code =
        """class Test {
          | BigDecimal(0.1d)
          | val d = 0.1d
          | BigDecimal(d)
          | BigDecimal(100) // ok not a double
          | new java.math.BigDecimal(0.5d) // check java ones
          |} """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 3
    }
  }
}
