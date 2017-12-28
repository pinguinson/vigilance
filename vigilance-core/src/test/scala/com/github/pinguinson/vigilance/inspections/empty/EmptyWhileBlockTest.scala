package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class EmptyWhileBlockTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(EmptyWhileBlock)

  "empty while block" - {
    "should report warning" in {

      val code =
        """object Test {
          |   while(true) {}
          |} """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
  "not empty while block" - {
    "should not report warning" in {

      val code =
        """object Test {
          |   while(true) { println("sam") }
          |} """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
  }
}
