package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class PublicFinalizerTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(PublicFinalizer)

  "public finalizer" - {
    "should report warning" - {
      "for a public overridden finalize method" in {
        val code = """class Test {
                        override def finalize : Unit = ()
                      }""".stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
    "should not report warning" - {
      "for a protected overridden finalize method" in {
        val code = """class Test {
                        override protected def finalize : Unit = ()
                      }""".stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
