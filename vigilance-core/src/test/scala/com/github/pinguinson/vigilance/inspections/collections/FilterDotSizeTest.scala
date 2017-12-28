package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers }

/** @author Stephen Samuel */
class FilterDotSizeTest
    extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(FilterOperations)

  "filter then size" - {
    "should report warning" in {

      val code = """object Test {
                      val list = List(1,2,3,4).filter(_ % 2 == 0).size
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
}
