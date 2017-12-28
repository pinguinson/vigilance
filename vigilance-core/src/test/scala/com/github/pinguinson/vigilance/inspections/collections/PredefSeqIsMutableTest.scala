package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner

import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class PredefSeqIsMutableTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(PredefMutableCollection)

  "PredefSeqUse" - {
    "should report warning" - {
      "for predef seq apply" in {
        val code = """object Test { val a = Seq("sammy") }""".stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
      "for declaring Seq as return type" in {
        val code = """object Test { def foo : Seq[String] = ??? }""".stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
    "should not report warning" - {
      "for scala.collection.mutable usage" in {
        val code = """import scala.collection.mutable.Seq
                     |object Test { val a = Seq("sammy") }""".stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for scala.collection.immutable usage" in {
        val code = """import scala.collection.immutable.Seq
                     |object Test { val a = Seq("sammy") }""".stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for scala.collection.mutable defs" in {
        val code = """import scala.collection.mutable.Seq
                     |object Test { def foo : Seq[String] = ??? }""".stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for scala.collection.immutable defs" in {
        val code = """import scala.collection.immutable.Seq
                     |object Test { def foo : Seq[String] = ??? }""".stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
