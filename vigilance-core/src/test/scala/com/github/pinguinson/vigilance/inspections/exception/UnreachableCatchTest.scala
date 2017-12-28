package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class UnreachableCatchTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(CatchOperations)

  "unreachable catch" - {
    "should report warning" - {
      "for subtype after supertype" in {

        val code1 = """object Test {
                        try {
                        } catch {
                          case _ : Throwable =>
                          case e : Exception => // not reachable
                        }
                    } """.stripMargin

        compileCodeSnippet(code1)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
    "should not report warning" - {
      "for super type after sub type" in {

        val code2 = """object Test {
                        try {
                        } catch {
                          case e : RuntimeException =>
                          case f : Exception =>
                          case x : Throwable =>
                        }
                    } """.stripMargin

        compileCodeSnippet(code2)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}