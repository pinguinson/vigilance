package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Marconi Lanna */
class CatchFatalTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(CatchOperations)

  "catch _ fatal exception" - {
    "should report warning" in {

      val code1 = """object Test {
                        try {
                        } catch {
                          case _ : VirtualMachineError =>
                        }
                    } """.stripMargin

      compileCodeSnippet(code1)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
  "catch e fatal exception" - {
    "should report warning" in {

      val code2 = """object Test {
                        try {
                        } catch {
                          case x : ThreadDeath =>
                        }
                    } """.stripMargin

      compileCodeSnippet(code2)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
  "catch without fatal exception case" - {
    "should not report warning" in {

      val code2 = """object Test {
                        try {
                        } catch {
                          case e : NoSuchElementException =>
                        }
                    } """.stripMargin

      compileCodeSnippet(code2)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
  }
}
