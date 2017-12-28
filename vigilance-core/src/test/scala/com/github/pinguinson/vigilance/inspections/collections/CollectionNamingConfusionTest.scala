package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.PluginRunner
import org.scalatest.{ FreeSpec, Matchers, OneInstancePerTest }

/** @author Stephen Samuel */
class CollectionNamingConfusionTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(CollectionNamingConfusion)

  "collection confusing names" - {
    "should report warning" in {
      val code = """object Test {
                      val set = List(1)
                      val mySet = List(2)
                      val mySetWithStuff = List(3)
                      val list = Set(1)
                      val myList = Set(2)
                      val myListWithStuff = Set(3)
                    } """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 6
    }
  }
}
