package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class CatchNpeTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(CatchOperations)

  "catching null pointer exception" - {
    "should report warning" in {

      val code1 = """object Test {
                        try {
                          var s : String = null
                          s.toString
                        } catch {
                          case e : NullPointerException =>
                        }
                    } """.stripMargin

      compileCodeSnippet(code1)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
  "catching non npe" - {
    "should not report warning" in {

      val code2 = """object Test {
                        try {
                          var s : String = null
                          s.toString
                        } catch {
                          case e : RuntimeException =>
                        }
                    } """.stripMargin

      compileCodeSnippet(code2)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
  }
}