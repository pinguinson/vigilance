package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers }

/** @author Stephen Samuel */
class FindDotIsDefinedTest
    extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(FindOperations)

  "filter then size" - {
    "should report warning" in {

      val code = """object Test {
                      val a = List(1,2,3).find(_>4).isDefined
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
}
