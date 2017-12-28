package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.inference.MethodReturningAny
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class MethodReturningAnyTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(MethodReturningAny)

  "MethodReturningAny" - {
    "should report warning" - {
      "for methods returning any" in {

        val code = """class Test {
                        def foo : Any = 1
                    } """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
    "should not report warning" - {
      "for methods returning <:< Any" in {

        val code =
          """class Test {
            | def foo : Int = 4
            | def boo : String = "sam"
            |} """.stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for overridden methods returning any" in {
        val code =
          """object T {
               trait A {
                 def foo : AnyRef = "foo"
               }
               class B extends A {
                 override def foo : AnyRef = "overridden foo"
               }
            |} """.stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
  }
}