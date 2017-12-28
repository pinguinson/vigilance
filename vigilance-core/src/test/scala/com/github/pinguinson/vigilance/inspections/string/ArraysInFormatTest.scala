package com.github.pinguinson.vigilance.inspections.string

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class ArraysInFormatTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(ArraysInFormat)

  "use of array in format" - {
    "should report warning" in {
      val code =
        """class Test {
            val array = Array("sam")
            "%s".format(array)
            "%s".format(Array("inline"))
            "%s".format(List("I'm deep")) // list is fine
          } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 2
    }
  }
}
