package com.github.pinguinson.vigilance.inspections

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.unsafe.TryGet
import org.scalatest.{ FreeSpec, Matchers }

/** @author Stephen Samuel */
class TryGetTest extends FreeSpec with PluginRunner with Matchers {

  override val inspections = Seq(TryGet)

  "try.get use" - {
    "should report warning" in {

      val code = """class Test {
                      import scala.util.Try
                      Try {
                        println("sam")
                      }.get
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
}
