package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class AnyUseTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(AnyUse)

  "AnyUse" - {
    "should report warning" - {
      "for methods returning any" in {

        val code = """class Test {
                       def foo =  {
                         if (1 < System.currentTimeMillis) {
                           "sam"
                         } else {
                            4
                         }
                       }
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
      "for vals returning any" in {

        val code = """class Test {
                       val foo =  {
                         if (1 < System.currentTimeMillis) {
                           "sam"
                         } else {
                            4
                         }
                       }
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
      "for lazy vals returning any" in {

        val code = """class Test {
                       lazy val foo =  {
                         if (1 < System.currentTimeMillis) {
                           "sam"
                         } else {
                            4
                         }
                       }
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
      "for vars returning any" in {

        val code = """class Test {
                       var foo =  {
                         if (1 < System.currentTimeMillis) {
                           "sam"
                         } else {
                            4
                         }
                       }
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
  }
}