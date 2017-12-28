package com.github.pinguinson.vigilance.inspections.math

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers }

/** @author Matic Potočnik */
class UseCbrtTest extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(UseCbrt)

  "using pow instead of cbrt" - {
    "should report warning" in {

      val code = """object Test {
                        val a = 2
                        scala.math.pow(2, 1/3d)
                        math.pow(2, 1/3d)
                        scala.math.pow(2, 1/3f)
                        math.pow(2, 1/3f)
                        Math.pow(2, 1/3d)
                        StrictMath.pow(2, 1/3d)
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 6
    }
  }
}
