package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{FlatSpec, Matchers, OneInstancePerTest}

class TraversableOnceUnsafeTest extends FlatSpec with Matchers with OneInstancePerTest with PluginRunner {

  override val inspections = TraversableOnceUnsafe.All

  "TraversableOnceMin" should "report error" in {
    val code =
      """
        |class Test {
        |  Seq(1, 2, 3).min
        |}
      """.stripMargin
    compileCodeSnippet(code)
    compiler.vigilance.feedback.reports.size shouldBe 1
  }

  "TraversableOnceMax" should "report error" in {
    val code =
      """
        |class Test {
        |  Seq(1, 2, 3).max
        |}
      """.stripMargin
    compileCodeSnippet(code)
    compiler.vigilance.feedback.reports.size shouldBe 1
  }

  "TraversableOnceMinBy" should "report error" in {
    val code =
      """
        |class Test {
        |  Seq(1, 2, 3).minBy(-_)
        |}
      """.stripMargin
    compileCodeSnippet(code)
    compiler.vigilance.feedback.reports.size shouldBe 1
  }

  "TraversableOnceMaxBy" should "report error" in {
    val code =
      """
        |class Test {
        |  Seq(1, 2, 3).maxBy(-_)
        |}
      """.stripMargin
    compileCodeSnippet(code)
    compiler.vigilance.feedback.reports.size shouldBe 1
  }

  "TraversableOnceReduceLeft" should "report error" in {
    val code =
      """
        |class Test {
        |  Seq(1, 2, 3).reduceLeft(_ + _)
        |}
      """.stripMargin
    compileCodeSnippet(code)
    compiler.vigilance.feedback.reports.size shouldBe 1
  }

  "TraversableOnceReduceRight" should "report error" in {
    val code =
      """
        |class Test {
        |  Seq(1, 2, 3).reduceRight(_ + _)
        |}
      """.stripMargin
    compileCodeSnippet(code)
    compiler.vigilance.feedback.reports.size shouldBe 1
  }
}
