package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class ListSizeTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(ListSize)

  "list.size use" - {
    "should report warning" - {
      val code = """class Test {
                     List(1,2,3).size
                     val a = List("sam")
                     a.size
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 2
    }
  }
}
