package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ OneInstancePerTest, FreeSpec, Matchers }

class TraversableLastTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(TraversableLast)

  "option.last use" - {
    "should report warning" in {

      val code = """class Test {
                      Seq("sam").last
                      List("sam").last
                      Vector("sam").last
                      Iterable("sam").last
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 4
    }
    "should not report warning" - {
      "for var args" in {
        val code = """class F(args:String*)""".stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
