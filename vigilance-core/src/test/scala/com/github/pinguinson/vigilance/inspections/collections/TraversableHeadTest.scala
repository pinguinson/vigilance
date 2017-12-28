package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ OneInstancePerTest, FreeSpec, Matchers }

/** @author Stephen Samuel */
class TraversableHeadTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(TraversableHead)

  "option.head use" - {
    "should report warning" in {

      val code = """class Test {
                      Seq("sam").head
                      List("sam").head
                      Vector("sam").head
                      Iterable("sam").head
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