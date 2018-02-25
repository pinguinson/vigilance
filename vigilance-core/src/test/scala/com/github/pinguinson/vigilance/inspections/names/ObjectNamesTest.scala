package com.github.pinguinson.vigilance.inspections.names

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.naming.ObjectNames
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class ObjectNamesTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(ObjectNames)

  "ObjectNames" - {
    "should report warning" - {
      "for objects containing underscore" in {
        val code =
          """object My_class
            |case object Your_class
          """.stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 2
      }
    }
    "should not report warning" - {
      "for package objects" in {
        val code =
          """package object banana
          """.stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
