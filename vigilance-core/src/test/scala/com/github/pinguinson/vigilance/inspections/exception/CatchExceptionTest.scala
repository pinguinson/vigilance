package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Marconi Lanna */
class CatchExceptionTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(CatchOperations)

  "catch _ exception" - {
    "should report warning" in {

      val code1 = """object Test {
                        try {
                        } catch {
                          case _ : Exception =>
                        }
                    } """.stripMargin

      compileCodeSnippet(code1)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
  "catch e exception" - {
    "should report warning" in {

      val code2 = """object Test {
                        try {
                        } catch {
                          case e : RuntimeException =>
                        }
                    } """.stripMargin

      compileCodeSnippet(code2)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
  "catch without exception case" - {
    "should not report warning" in {

      val code2 = """object Test {
                        try {
                        } catch {
                          case e : RuntimeException =>
                          case f : NoSuchElementException =>
                        }
                    } """.stripMargin

      compileCodeSnippet(code2)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
  }
}
