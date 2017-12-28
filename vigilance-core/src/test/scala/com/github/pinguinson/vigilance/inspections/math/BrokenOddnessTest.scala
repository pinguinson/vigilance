package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers }

/** @author Stephen Samuel */
class BrokenOddnessTest extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(BrokenOddness)

  "broken odd use" - {
    "should report warning" in {

      val code = """object Test {
                   |var i = 15
                   |        def odd(a: Int) = a % 2 == 1
                   |        val odd2 = i % 2 == 1
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 2
    }
  }
}
