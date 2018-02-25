package com.github.pinguinson.vigilance.inspections.option

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{FlatSpec, Matchers, OneInstancePerTest}

class OptionMatchTest extends FlatSpec with Matchers with OneInstancePerTest with PluginRunner {

  override val inspections = Seq(OptionMatch)

  it should "report excessive match usage" in {
    val code =
      """
        |class Test {
        |  Option("banana1") match {
        |    case Some(str) => Some(str.reverse)
        |    case None => None
        |  }
        |}
      """.stripMargin
    compileCodeSnippet(code)
    compiler.vigilance.feedback.reports.size shouldBe 1
  }

  it should "not report if pattern match can't be replaced with map" in {
    val code =
      """
        |class Test {
        |  Option("banana2") match {
        |    case Some(str) => Some(str.reverse)
        |    case None => Some("empty")
        |  }
        |}
      """.stripMargin
    compileCodeSnippet(code)
    compiler.vigilance.feedback.reports.size shouldBe 0
  }

  it should "kek" in {
    val code =
      """
        |class Test {
        |  for {
        |    o1 <- Option("banana")
        |    o2 <- Option("apple")
        |  } yield o1 + o2
        |}
      """.stripMargin
    compileCodeSnippet(code)
    compiler.vigilance.feedback.reports.size shouldBe 0
  }

}
