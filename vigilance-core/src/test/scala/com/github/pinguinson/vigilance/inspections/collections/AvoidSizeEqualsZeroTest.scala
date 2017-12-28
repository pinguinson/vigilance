package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class AvoidSizeEqualsZeroTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(AvoidSizeEqualsZero)

  "collection.size != 0" - {
    "should report warning" in {
      val code =
        """object Test {
                      val isEmpty0 = List(1).size != 0
                      val isEmpty1 = List(1).length != 0
                      val isEmpty2 = Set(1).size != 0
                      val isEmpty3 = Seq(1).size != 0
                      val isEmpty4 = Seq(1).length != 0
                      val isEmpty5 = List(1).size == 0
                      val isEmpty6 = List(1).length == 0
                      val isEmpty7 = Set(1).size == 0
                      val isEmpty8 = Seq(1).size == 0
                      val isEmpty9 = Seq(1).length == 0
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 10
    }

    // github issue #94
    "should ignore durations" in {
      val code =
        """object Test {
          |  case class Duration(start: Long, stop: Long) {
          |    def length: Long = stop - start
          |    def isEmpty: Boolean =  length == 0
          |    def nonEmpty: Boolean = length != 0
          |  }
          |} """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
  }
}
