package com.github.pinguinson.vigilance.inspections.unnecessary

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.unneccesary.UnnecessaryReturnUse
import org.scalatest.{FreeSpec, Matchers}

/** @author Stephen Samuel */
class UnnecessaryReturnUseTest extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(UnnecessaryReturnUse)

  "return keyword use" - {
    "should report warning" in {

      val code = """class Test {
                      def hello : String = {
                        val s = "sammy"
                        return s
                      }
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
}
