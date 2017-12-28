package com.github.pinguinson.vigilance.inspections.nulls

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class NullAssignmentTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(NullAssignment)

  "NullAssignment" - {
    "should report warning" - {
      "for assigning null to var" in {
        val code = """object Test {
                        var a = "sam"
                        a = null
                      } """.stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
      "for assigning null to a local var" in {
        val code = """object Test {
                       def foo {
                        var a = "sam"
                        a = null
                       }
                      } """.stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
      "for assigning null to val" in {
        val code = """object Test {
                        val b : String = null
                      } """.stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
    "should not report warning" - {
      "for override val in case class" in {
        val code = """abstract class Super(val name: String)
                    case class Boo(override val name: String) extends Super(name) """
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for var args" in {
        val code = """class Birds(names:String*)"""
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}