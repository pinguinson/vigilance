package com.github.pinguinson.vigilance.inspections.equality

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class ComparingFloatingPointTypesTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(ComparingFloatingPointTypes)

  "comparing floating type inspection" - {
    "should report warning" - {
      "for double comparison" in {
        val code = """class Test {
                      def compareFloats : Boolean = {
                        val a = 14.5
                        val b = 15.6
                        a == b
                      }
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
      "for float comparison" in {
        val code = """class Test {
                      def compareFloats : Boolean = {
                        val a = 14.5f
                        val b = 15.6f
                        a == b
                      }
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
  }
}
