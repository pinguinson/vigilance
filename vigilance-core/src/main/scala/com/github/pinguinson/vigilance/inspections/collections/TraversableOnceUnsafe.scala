package com.github.pinguinson.vigilance.inspections.collections

import com.github.pinguinson.vigilance.{Inspection, InspectionContext, Inspector, Levels}

abstract class TraversableOnceUnsafe(inspectionDescription: String, termName: String, comment: String) extends Inspection {

  override val level = Levels.Error
  override val description = inspectionDescription

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case SelectTraversableLike(TermName(`termName`)) | Apply(SelectTraversableOnce(TermName(`termName`)), _) =>
          context.warn(tree.pos, self, comment)
      }
    }
  }
}

object TraversableOnceMin extends TraversableOnceUnsafe(
  inspectionDescription = "Use of TraversableOnce.min",
  termName = "min",
  comment = "TraversableOnce.min throws exception on empty collection"
)

object TraversableOnceMax extends TraversableOnceUnsafe(
  inspectionDescription = "Use of TraversableOnce.max",
  termName = "max",
  comment = "TraversableOnce.max throws exception on empty collection"
)

object TraversableOnceMinBy extends TraversableOnceUnsafe(
  inspectionDescription = "Use of TraversableOnce.minBy",
  termName = "minBy",
  comment = "TraversableOnce.minBy throws exception on empty collection"
)

object TraversableOnceMaxBy extends TraversableOnceUnsafe(
  inspectionDescription = "Use of TraversableOnce.maxBy",
  termName = "maxBy",
  comment = "TraversableOnce.maxBy throws exception on empty collection"
)

object TraversableOnceReduceLeft extends TraversableOnceUnsafe(
  inspectionDescription = "Use of TraversableOnce.reduceLeft",
  termName = "reduceLeft",
  comment = "TraversableOnce.reduceLeft throws exception on empty collection"
)

object TraversableOnceReduceRight extends TraversableOnceUnsafe(
  inspectionDescription = "Use of TraversableOnce.reduceRight",
  termName = "reduceRight",
  comment = "TraversableOnce.reduceRight throws exception on empty collection"
)

object TraversableOnceUnsafe {
  val All = Seq(TraversableOnceMin, TraversableOnceMax, TraversableOnceMinBy, TraversableOnceMaxBy, TraversableOnceReduceLeft, TraversableOnceReduceRight)
}