package com.github.pinguinson.vigilance.inspections.matching

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{FlatSpec, Matchers, OneInstancePerTest}

class ExcessiveGuardUsageTest
    extends FlatSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(ExcessiveGuardUsage)

  it should "report for simple types" in {
    val code =
      """
        |class Test {
        |  "banana" match {
        |    case str if str == "banana" => "banana!"
        |    case str if "banana" == str => "!banana"
        |  }
        | }
      """.stripMargin

    compileCodeSnippet(code)
    compiler.vigilance.feedback.reports.size shouldBe 2
  }

  it should "report for case classes" in {
    val code =
      """
        |class Test {
        |  case class Foo(bar: String, baz: String)
        |  Foo("bar", "baz") match {
        |    case foo if foo == Foo("bar", "baz") => "banana!"
        |    case foo if Foo("bar", "baz") == foo => "!banana"
        |  }
        | }
      """.stripMargin

    compileCodeSnippet(code)
    compiler.vigilance.feedback.reports.size shouldBe 2
  }
}
