package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class SubstringZeroTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(SubstringZero)

  "String.substring(0)" - {
    "should report warning" in {

      val code = """object Test {
                      val name = "sam"
                      name.substring(0)
                   } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
  "String.substring(function)" - {
    "should not report warning" in {

      val code = """object Test {
                     val name = "sam"
                     def index = 0
                     name.substring(index)
                   } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
  }
}
