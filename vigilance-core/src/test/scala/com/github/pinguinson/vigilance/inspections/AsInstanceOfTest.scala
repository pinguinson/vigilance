package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.unsafe.InstanceOf
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

class AsInstanceOfTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(InstanceOf)

  "AsInstanceOf" - {
    "should report warning" in {
      val code = """class Test {
                      def hello : Unit = {
                        val s : Any = "sammy"
                        println(s.asInstanceOf[String])
                      }
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
    "should ignore case classes synthetic methods" in {
      val code = """case class MappingCharFilter(name: String, mappings: (String, String)*)""".stripMargin
      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
    "should ignore partial functions" in {
      val code =
        """object Test {
          |val pf :PartialFunction[Any,Unit] = {
          |  case s : String => println(s)
          |  case i : Int if i == 4 => println(i)
          |  case _ => println("no match :(")
          |}
          |}
        """.stripMargin
      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
    "should ignore @SuppressWarnings when all is set" in {
      val code = """class Test {
                      @SuppressWarnings(Array("all"))
                      def hello : Unit = {
                        val s : Any = "sammy"
                        println(s.asInstanceOf[String])
                      }
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
    "should ignore @SuppressWarnings when this inspection is set" in {
      val code = """class Test {
                          @SuppressWarnings(Array("instanceOf"))
                          def hello : Unit = {
                            val s : Any = "sammy"
                            println(s.asInstanceOf[String])
                          }
                        } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0

      val mf = manifest[Class[_]]

    }
    "should not warn on manifest of class" in {
      val code = """object Test {
                      @SuppressWarnings(Array("instanceOf"))
                      val mf = manifest[Class[_]]
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }

  }

  "isInstanceOf" - {

    "should report warning" in {
      val code = """class Test {
                      def hello : Unit = {
                        val s : Any = "sammy"
                        println(s.asInstanceOf[String])
                      }
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
    "should ignore case classes synthetic methods" in {
      val code = """case class MappingCharFilter(name: String, mappings: (String, String)*)""".stripMargin
      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
    "should ignore partial functions" in {
      val code =
        """object Test {
          |val pf :PartialFunction[Any,Unit] = {
          |  case s : String => println(s)
          |  case i : Int if i == 4 => println(i)
          |  case _ => println("no match :(")
          |}
          |}
        """.stripMargin
      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
    "should ignore @SuppressWarnings when all is set" in {
      val code = """class Test {
                      @SuppressWarnings(Array("all"))
                      def hello : Unit = {
                        val s : Any = "sammy"
                        println(s.asInstanceOf[String])
                      }
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
    "should ignore @SuppressWarnings when this inspection is set" in {
      val code = """class Test {
                          @SuppressWarnings(Array("instanceof"))
                          def hello : Unit = {
                            val s : Any = "sammy"
                            println(s.asInstanceOf[String])
                          }
                        } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0

      val mf = manifest[Class[_]]

    }
    "should not warn on manifest of class" in {
      val code = """object Test {
                      @SuppressWarnings(Array("instanceof"))
                      val mf = manifest[Class[_]]
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 0
    }
  }
}
