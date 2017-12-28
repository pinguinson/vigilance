package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers }

class UseLog1PTest extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(UseLog1P)

  "using log(x + 1) instead of log1p(x)" - {
    "should report warning" in {

      val code = """object Test {
                        val a = 2d
                        scala.math.log(a + 1)
                        math.log(a + 1)
                        scala.math.log(1 + a)
                        math.log(1 + a)
                        Math.log(a + 1)
                        StrictMath.log(a + 1)
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 6
    }
  }
}
