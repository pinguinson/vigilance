package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.unsafe.FinalizerWithoutSuper
import org.scalatest.{ OneInstancePerTest, FreeSpec, Matchers }

/** @author Stephen Samuel */
class FinalizerWithoutSuperTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(FinalizerWithoutSuper)

  "FinalizerWithoutSuper" - {
    "should report warning" - {
      "when empty finalizer" in {
        val code =
          """class Test {
            |  override def finalize {
            |  }
            |} """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
      "when non finalizer" in {
        val code =
          """class Test {
            |  override def finalize {
            |    println("sam")
            |  }
            |} """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
  }
  "FinalizerWithoutSuper" - {
    "should not report warning" - {
      "when def contains super.finalize() as first call" in {
        val code =
          """class Test {
            |  override def finalize {
            |    super.finalize()
            |    println("sam")
            |  }
            |} """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "when def contains super.finalize() as intermediate call" in {
        val code =
          """class Test {
            |  override def finalize {
            |    println("sam")
            |    super.finalize()
            |    println("sam")
            |  }
            |} """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "when def contains super.finalize() as final call" in {
        val code =
          """class Test {
            |  override def finalize {
            |    println("sam")
            |    super.finalize()
            |  }
            |} """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
