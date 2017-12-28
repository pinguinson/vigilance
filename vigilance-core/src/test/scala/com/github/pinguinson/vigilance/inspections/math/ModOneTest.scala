package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers }

/** @author Stephen Samuel */
class ModOneTest extends FreeSpec with PluginRunner with Matchers {

  override val inspections = Seq(ModOne)

  "mod one use" - {
    "should report warning" in {

      val code = """object Test {
                     var i = 15
                     def odd(a: Int) = a % 1
                     val odd2 = i % 1
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 2
    }
  }
}
