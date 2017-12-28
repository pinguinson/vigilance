package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class FindAndNotEqualsNoneReplaceWithExistsTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(FindOperations)

  "FindAndNotEqualsNoneReplaceWithExists" - {
    "should report warning" - {
      "for find and not equals" in {
        val code = """object Test { List(1,2,3).find(_ > 0) != None } """
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
    "should not report warning" - {
      "for find" in {
        val code = """object Test { List(1,2,3).find(_ > 0) } """
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for != None" in {
        val code = """object Test { List(1,2,3) != None } """
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for find equals None" in {
        val code = """object Test { List(1,2,3).find(_ > 0) == None } """
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
