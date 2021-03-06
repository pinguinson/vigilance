package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ OneInstancePerTest, FreeSpec, Matchers }

/** @author Stephen Samuel */
class EmptyForTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(EmptyFor)

  "EmptyFor" - {
    "should report warning" in {

      val code = """object Test {
                     for (k <- 1 to 100) {
                     }
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
    "should not report warning" - {
      "for non empty loop" in {
        val code = """object Test {
                     for (k <- 1 to 100) {
                       println("sam")
                     }
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
