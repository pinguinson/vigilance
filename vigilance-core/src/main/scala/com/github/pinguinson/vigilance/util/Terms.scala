package com.github.pinguinson.vigilance.util

trait Terms {
  self: CompilerAccess =>

  import global._

  val Finalize = TermName("finalize")
  val IsInstanceOf = TermName("isInstanceOf")
  val AsInstanceOf = TermName("asInstanceOf")
  val Append = TermName("$colon$plus")
  val Head = TermName("head")
  val Last = TermName("last")
  val Reverse = TermName("reverse")
  val Contains = TermName("contains")
  val Map = TermName("Map")
  val Get = TermName("get")
  val GetOrElse = TermName("getOrElse")
  val TermApply = TermName("apply")
  val Equals = TermName("$eq$eq")
  val NotEquals = TermName("$bang$eq")
  val Bang = TermName("unary_$bang")
  val Empty = TermName("empty")
  val TermNil = TermName("Nil")
  val TermList = TermName("List")
  val TermSet = TermName("Set")
  val Filter = TermName("filter")
  val Find = TermName("find")
  val HeadOption = TermName("headOption")
  val IsEmpty = TermName("isEmpty")
  val IsDefined = TermName("isDefined")
  val NonEmpty = TermName("nonEmpty")
  val Size = TermName("size")
  val Exists = TermName("exists")
  val Foreach = TermName("foreach")
  val Minus = TermName("$minus")
  val To = TermName("to")
  val Right = TermName("right")
  val Left = TermName("left")
  val ToString = TermName("toString")
}
