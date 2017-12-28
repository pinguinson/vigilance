package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers }

/** @author Matic Potočnik */
class UseExpM1Test extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(UseExpM1)

  "using exp(x) - 1 instead of expm1(x)" - {
    "should report warning" in {

      val code = """object Test {
                        val a = 2d
                        math.exp(a) - 1
                        Math.exp(a) - 1
                        StrictMath.exp(a) - 1
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 3
    }
  }
}
