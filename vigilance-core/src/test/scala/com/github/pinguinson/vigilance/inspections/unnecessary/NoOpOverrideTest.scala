package com.github.pinguinson.vigilance.inspections.unnecessary

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.NoOpOverride
import com.github.pinguinson.vigilance.inspections.unneccesary.UnnecessaryToInt
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class NoOpOverrideTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(NoOpOverride)

  "UnnecessaryOverride" - {
    "should report warning" - {
      "when overriding method to just call super" in {

        val code =
          """class Test {
            |  override def finalize() {
            |    super.finalize()
            |  }
            |}
          """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
    "should not report warning" - {
      "when overriding method with super call and other code" in {

        val code =
          """class Test {
            |  override def finalize() {
            |    super.finalize()
            |    println("sam")
            |  }
            |}
          """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
