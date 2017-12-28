package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class EmptyInterpolatedStringTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(EmptyInterpolatedString)

  "EmptyInterpolatedString" - {
    "should report warning" in {

      val code = """object Test {
                      val name = "sam"
                      s"hello name"
                   } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
  "non empty interpolated string" - {
    "should not report warning" in {

      val code = """object Test {
                     val name = "sam"
                      s"hello $name"
                   } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
  }
}
