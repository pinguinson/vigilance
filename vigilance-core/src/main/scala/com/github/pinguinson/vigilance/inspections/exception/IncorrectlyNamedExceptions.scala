package com.github.pinguinson.vigilance.inspections.exception

import com.github.pinguinson.vigilance._

/**
  * @author Stephen Samuel
  *
  *         Inspired by http://findbugs.sourceforge.net/bugDescriptions.html#NM_CLASS_NOT_EXCEPTION
  */
class IncorrectlyNamedExceptions extends Inspection { self =>

  override val level = Levels.Error
  override val description = "Exception naming"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      override def inspect(tree: Tree) = {
        case classDef @ ClassDef(_, name, _, impl) =>
          val isNamedException = name.toString.endsWith("Exception")
          val isAnon = Option(classDef.symbol).exists(_.isAnonymousClass)

          val extendsException = impl.tpe <:< typeOf[Exception]
          val selfTypeIsException = impl match {
            case Template(_, self, _) => self.tpt.tpe <:< typeOf[Exception]
            case _ => false
          }

          // A class or trait is an Exception for our purposes if it either
          // inherits from exception or it is a trait which declares its
          // self-type to be Exception
          val isException = extendsException || selfTypeIsException

          (isNamedException, isAnon, isException) match {
            case (true, _, false) | (false, false, true) =>
              context.warn(tree.pos, self, "Exceptions should be derived from Exception and be named *Exception")
            case _ =>
          }
      }
    }
  }
}