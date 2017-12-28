package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers }

/** @author Stephen Samuel */
class EmptySynchronizedBlockTest extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(EmptySynchronizedBlock)

  "empty empty" - {
    "should report warning" in {

      val code = """class Test {
                   |  synchronized {
                   |    println("sammy")
                   |  }
                   |  val a = {
                   |    println("sammy")
                   |  }
                   |  synchronized {
                   |  }
                   |  val b = {
                   |  }
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
}
