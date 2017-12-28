package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class DuplicateSetValueTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(DuplicateSetValue)

  "duplicate set literals" - {
    "should report warning" in {
      val code = """object Test {
                      Set("sam", "aylesbury", "sam")
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }

  "non duplicate set literals" - {
    "should not report warning" in {
      val code = """object Test {
                      Set("name", "location", "aylesbury", "bob")
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
  }

  "duplicate etas" - {
    "should not report warning" in {
      val code = """object Test {
                      def name = "could be random"
                      Set(name, "middle", name)
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
  }
}
