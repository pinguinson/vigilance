package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class CatchThrowableTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(CatchOperations)

  "catch _ throwable" - {
    "should report warning" in {

      val code1 = """object Test {
                        try {
                        } catch {
                          case e : Exception =>
                          case _ : Throwable =>
                        }
                    } """.stripMargin

      compileCodeSnippet(code1)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
  "catch e throwable" - {
    "should report warning" in {

      val code2 = """object Test {
                        try {
                        } catch {
                          case e : RuntimeException =>
                          case x : Throwable =>
                          case f : Exception =>
                        }
                    } """.stripMargin

      compileCodeSnippet(code2)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
  "catch without throwable case" - {
    "should not report warning" in {

      val code2 = """object Test {
                        try {
                        } catch {
                          case e : RuntimeException =>
                          case f : Exception =>
                        }
                    } """.stripMargin

      compileCodeSnippet(code2)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
  }
}
