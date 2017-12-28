package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class TypeShadowingTest
    extends FreeSpec
    with Matchers
    with PluginRunner
    with OneInstancePerTest {

  override val inspections = Seq(TypeShadowing)

  "TypeShadowing" - {
    "should report warning" - {
      "when a method defines a shadowed type parameter" in {

        val code =
          """class Test[T, U] {
            |  def foo[T] = {}
            |  def boo[A, U] = {}
            |}""".stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 2
      }
      "when a nested classes method defines a shadowed type parameter" in {

        val code =
          """class Test[T, U] {
            |  def foo[G] = {}
            |  def boo[H] = {}
            |  class Nested[S] {
            |    def goo[S] = {}
            |  }
            |}""".stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
      "when a trait defines a shadowed type parameter" in {

        val code =
          """trait Test[T, U] {
            |  def foo[A,T,B] = {}
            |}""".stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 1
      }
    }
    "should not report warning" - {
      "when method use class types as type bounds" in {
        val code =
          """class Test[T,U] {
                 def foo[B] = {}
                 def goo[C >: U] = {}
                 def boo[D <: T] = {}
             }""".stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "when nested class shadows type parameter" in {
        val code =
          """class Test[T,U] {
                 def foo[B] = {}
                 def goo[C >: U] = {}
                 def boo[D <: T] = {}
                 class Nested[T] {
                   def hoo[K] = {}
                 }
             }""".stripMargin

        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for case class synthetic methods" in {
        val code =
          """
            |case class CombinedInfo[A](
            |  combined: A,
            |  extraIPs: Set[Long],
            |  extraHosts: Set[String],
            |  notes: Set[String] = Set.empty)
          """.stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}
