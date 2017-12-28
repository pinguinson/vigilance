package com.github.pinguinson.vigilance.inspections.unnecessary

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.unneccesary.UnnecessaryIf
import org.scalatest.{FreeSpec, Matchers, OneInstancePerTest}

/** @author Stephen Samuel */
class UnnecessaryIfTest
    extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(UnnecessaryIf)

  "unncessary if" - {
    "should report warning" in {

      val code = """object Test {
                      val a = "sam"
                      if (a == "sam") true else false
                      if (a == "sam") false else true
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 2
    }
    "should not report warning" - {
      "in empty case classes" in {
        val code =
          """object Test {
               case class DebuggerShutdownEvent()
             }
          """.stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
