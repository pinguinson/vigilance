package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers }

class UseLog10Test extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(UseLog10)

  "using log(x)/log(10) instead of log10(x)" - {
    "should report warning" in {

      val code = """object Test {
                        val a = 2
                        scala.math.log(a)/scala.math.log(10)
                        math.log(a)/math.log(10)
                        scala.math.log(a+4)/scala.math.log(10d)
                        math.log(a+4)/math.log(10d)
                        Math.log(a)/Math.log(10)
                        StrictMath.log(a)/StrictMath.log(10)
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 6
    }
  }
}
