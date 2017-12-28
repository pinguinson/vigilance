package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.unneccesary.ConstantIf

import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class ConstantIfTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(ConstantIf)

  "ConstantIf" - {
    "should report warning" in {
      val code = """object Test {
                      if (1 < 2) {
                        println("sammy")
                      }
                      if (2 < 1) {
                        println("sammy")
                      }
                      if ("sam" == "sam".substring(0)) println("sammy")
                      if (true) println("sammy")
                      if (false) println("sammy")
                      if (1 < System.currentTimeMillis()) println("sammy")
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 4
    }
    "should not report warning" - {
      "for while loops" in {
        val code = """object Test { while ( true ) { println("sam") } } """
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
      "for subclasses of TypeCreator" in {
        val code =
          """import scala.reflect.api.{Mirror, Universe, TypeCreator}
            |class Test extends TypeCreator {
            |  override def apply[U <: Universe with Singleton](m: Mirror[U]): U#Type = {
            |    if (1 < 2)
            |      throw new RuntimeException
            |    else
            |      null
            |  }
            |}
          """.stripMargin
        compileCodeSnippet(code)
        compiler.vigilance.feedback.reports.size shouldBe 0
      }
    }
  }
}

