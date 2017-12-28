package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class NegativeSeqPadTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(NegativeSeqPad)

  "self assignment" - {
    "should report warning" - {
      val code = """class Test {
                     Seq(1,2,3).padTo(-1, 4)
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
}
