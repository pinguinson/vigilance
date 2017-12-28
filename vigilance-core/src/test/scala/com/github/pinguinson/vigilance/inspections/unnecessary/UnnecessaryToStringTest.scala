package com.github.pinguinson.vigilance.inspections.unnecessary

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.unneccesary.UnnecessaryToString
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class UnnecessaryToStringTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(UnnecessaryToString)

  "Unnecessary toString" - {
    "should report warning" - {
      "when invoking toString on a String" in {

        val code =
          """object Test {
                      val i = "sam"
                      val j = i.toString
                    }""".stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
    "should not report warning" - {
      "when invoking toString on a BigDecimal" in {
        val code =
          """object Test {
                      val s = BigDecimal(5)
                      val t = s.toString
                    }""".stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
