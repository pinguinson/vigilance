package com.github.pinguinson.vigilance.util

import scala.collection.{MapLike, TraversableLike}

trait Extractors {
  self: CompilerAccess =>

  import global._

  trait SelectT[A] {
    def unapply(tree: Tree)(implicit typeTag: TypeTag[A]): Option[Name] = tree match {
      case Select(lhs, rhs) if lhs.tpe <:< typeOf[A] => Some(rhs)
      case _ => None
    }
  }

  object SelectOption extends SelectT[Option[_]]
  object SelectTry extends SelectT[scala.util.Try[_]]
  object SelectTraversable extends SelectT[TraversableLike[_, _]]
  object SelectList extends SelectT[List[_]]
  object SelectSeq extends SelectT[Seq[_]]
  object SelectEither extends SelectT[Either[_, _]]
  object SelectMap extends SelectT[MapLike[_, _, _]]
  object SelectAny extends SelectT[Any]

  object TreeFloating {
    def unapply(tree: Tree): Option[Tree] = tree match {
      case t if tree.tpe <:< typeOf[Double] || tree.tpe <:< typeOf[Float] => Some(t)
      case _ => None
    }
  }

  def isInService(tree: Tree) = {
    tree.pos.source.file.name.endsWith("Service.scala")
  }
}
