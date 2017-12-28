package com.github.pinguinson.vigilance.inspections.option

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ OneInstancePerTest, FreeSpec, Matchers }

/** @author Stephen Samuel */
class ImpossibleOptionSizeConditionTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(ImpossibleOptionSizeCondition)

  "options.size > x where x is > 1" - {
    "should report warning" in {

      val code = """class Test {
                      val o = Option("sammy")
                      val e = o.size > 2
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
  "options.size > x where x is <= 1" - {
    "should not report warning" in {

      val code = """class Test {
                      val o = Option("sammy")
                      val e = o.size > 1
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
  }
}
