package com.github.pinguinson.vigilance.inspections.style

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers }

/** @author Stephen Samuel */
class SimplifyBooleanExpressionTest extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(SimplifyBooleanExpression)

  "incorrectly named exceptions" - {
    "should report warning" in {

      val code = """object Test {
                      val b = false
                      val f = b == false
                    }
                    """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
}
