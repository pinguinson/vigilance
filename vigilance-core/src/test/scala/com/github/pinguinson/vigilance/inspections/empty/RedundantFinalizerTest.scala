package com.github.pinguinson.vigilance.inspections.empty

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.unneccesary.RedundantFinalizer
import org.scalatest.{ FreeSpec, Matchers }

/** @author Stephen Samuel */
class RedundantFinalizerTest extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(RedundantFinalizer)

  "redundant finalizer" - {
    "should report warning" in {

      val code = """class Test {
                    override def finalize : Unit = {}
                    override def hashCode : Int = 3
                    def empty = {}
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
}
