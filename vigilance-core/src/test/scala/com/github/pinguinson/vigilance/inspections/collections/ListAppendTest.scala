package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class ListAppendTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(ListAppend)

  "list.append use" - {
    "should report warning" - {
      val code = """class Test {
                     List(1,2,3) :+ 4
                     val a = List("list")
                     a :+ "oh no"
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 2
    }
  }
}
