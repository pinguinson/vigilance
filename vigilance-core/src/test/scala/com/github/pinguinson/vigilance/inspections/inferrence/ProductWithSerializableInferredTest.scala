package com.github.pinguinson.vigilance.inspections.inferrence

import com.github.pinguinson.vigilance.PluginRunner
import com.github.pinguinson.vigilance.inspections.inference.ProductWithSerializableInferred
import org.scalatest.{ FreeSpec, Matchers }

/** @author Stephen Samuel */
class ProductWithSerializableInferredTest extends FreeSpec with Matchers with PluginRunner {

  override val inspections = Seq(ProductWithSerializableInferred)

  "when Product with Serializable is inferred" - {
    "should report warning" in {

      val code =
        """class Test {
          |case class A()
          |case class B()
          |val list1 = List(A(), B()) // trigger warning
          |val list2 = List(A()) // fine
          |val list3 = List(A()) :+ A() // fine
          |} """.stripMargin

      compileCodeSnippet(code)
      compiler.vigilance.feedback.reports.size shouldBe 1
    }
  }
}
