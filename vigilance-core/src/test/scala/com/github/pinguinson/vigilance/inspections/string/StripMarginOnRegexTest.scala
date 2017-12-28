package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance.PluginRunner

import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class StripMarginOnRegexTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(StripMarginOnRegex)

  "StripMarginOnRegex" - {
    "should report warning" - {
      "for regex containing | that calls strip margin before r" in {
        val code = """object Test {
                        val regex = "match|this".stripMargin.r
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
    "should not report warning" - {
      "for regex without | that calls strip margin before r" in {
        val code = """object Test {
                        val regex = "match_this".stripMargin.r
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for plain regex" in {
        val code = """object Test {
                        val regex = "match|this".r
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
