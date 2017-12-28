package com.github.pinguinson.vigilance.inspections.style

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ OneInstancePerTest, FreeSpec, Matchers }

/** @author Stephen Samuel */
class ParameterlessMethodReturnsUnitTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(ParameterlessMethodReturnsUnit)

  "ParameterlessMethodReturnsUnit" - {
    "should report warning" - {
      "for methods with unit and no params" in {

        val code =
          """object Test {
               def paramless: Unit = ()
             } """.stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
    "should not report warning" - {
      "for methods with unit and params" in {

        val code =
          """object Test {
               def params(): Unit = ()
             } """.stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for parameterless non unit methods" in {

        val code =
          """object Test {
               def params: Int = 3
             } """.stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
