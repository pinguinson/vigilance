package com.github.pinguinson.vigilance.inspections.unnecessary

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.unneccesary.UnnecessaryToInt
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class UnnecessaryToIntTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(UnnecessaryToInt)

  "Unnecessary to int" - {
    "should report warning" - {
      "when invoking toInt on an int" in {

        val code =
          """object Test {
                      val i = 4
                      val j = i.toInt
                    }""".stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
    "should not report warning" - {
      "when invoking toInt on a String" in {
        val code =
          """object Test {
                      val s = "5"
                      val t = s.toInt
                    }""".stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
