package com.github.pinguinson.vigilance.inspections.imports

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class WildcardImportTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(WildcardImport)

  "WildcardImport" - {
    "should report warning" - {
      "for wildcard imports" in {

        val code =
          """import scala.concurrent._
             object Test { }""".stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
  }
}
