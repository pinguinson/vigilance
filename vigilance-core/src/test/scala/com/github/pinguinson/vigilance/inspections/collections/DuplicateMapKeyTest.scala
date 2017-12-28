package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class DuplicateMapKeyTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(DuplicateMapKey)

  "DuplicateMapKey" - {
    "should report warning" in {
      val code = """object Test {
                      Map("name" -> "sam", "location" -> "aylesbury", "name" -> "bob")
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
    "should not report warning" - {
      "for duplicated strings" in {
        val code = """object Test {
                      Map("name" -> "sam", "location" -> "aylesbury", "name2" -> "bob")
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for var args" in {
        val code = """object Test {
                          val tuples = List((1,2), (3,4))
                          Map(tuples : _ *)
                      } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for duplicated tuples using unicode" in {

        val code = """object Test {
                     |        Map(
                     |          "hp" → "HP",
                     |          "x" → "x",
                     |          "xp" → "XP",
                     |          "ie" → "IE",
                     |          "gb" → "GB",
                     |          "mb" → "MB",
                     |          "kb" → "KB",
                     |          "sp" → "SP",
                     |          "nt" → "NT")
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
