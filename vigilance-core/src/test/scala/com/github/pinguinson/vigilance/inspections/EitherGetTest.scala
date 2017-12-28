package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.option.EitherGet
import org.scalatest.{ FreeSpec, Matchers }

class EitherGetTest extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(EitherGet)

  "either right / left projection get use" - {
    "should report warning" in {
      val code = """class Test {
                   |      val l = Left("l")
                   |      l.left.get
                   |      val r = Right("r")
                   |      r.right.get
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 2
    }
  }
}
