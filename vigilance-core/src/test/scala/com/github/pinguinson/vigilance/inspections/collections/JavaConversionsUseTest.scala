package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers }

/** @author Stephen Samuel */
class JavaConversionsUseTest extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(JavaConversionsUse)

  "JavaConversionsUse" - {
    "should report warning" in {

      val code = """import scala.collection.JavaConversions._
                    object Test {
                      val jul = new java.util.ArrayList[String]
                      val jum = new java.util.HashMap[String,String]
                      val a = jul.exists(_ == "sammy")
                   |  val b = jum.toSeq
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
}
