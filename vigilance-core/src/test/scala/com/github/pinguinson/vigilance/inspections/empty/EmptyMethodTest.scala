package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ OneInstancePerTest, FreeSpec, Matchers }

/** @author Stephen Samuel */
class EmptyMethodTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(EmptyMethod)

  "empty method" - {
    "should report warning" in {
      val code = """object Test {
                      def foo = { }
                      def foo2 = true
                      def foo3 = {
                        ()
                      }
                      def foo4 = {
                        println("sammy")
                        ()
                      }
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 2
    }
    "should not report warning" - {
      "for empty trait methods" in {
        val code = """trait A { def foo = () } """
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
