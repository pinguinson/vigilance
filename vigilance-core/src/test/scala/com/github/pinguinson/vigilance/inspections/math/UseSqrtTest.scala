package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers }

class UseSqrtTest extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(UseSqrt)

  "using pow instead of sqrt" - {
    "should report warning" in {

      val code = """object Test {
                        val a = 2
                        scala.math.pow(2, 0.5) // scala
                        scala.math.pow(2, 1/2d) // scala
                        math.pow(2, 0.5) // scala
                        math.pow(2, 1/2d) // scala
                        Math.pow(2, 1/2d) // java
                        StrictMath.pow(2, 1/2d) // java
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 6
    }
  }
}
