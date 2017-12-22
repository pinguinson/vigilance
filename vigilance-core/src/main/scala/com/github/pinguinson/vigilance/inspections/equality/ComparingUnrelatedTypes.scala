package com.github.pinguinson.vigilance.inspections.equality

import com.github.pinguinson.vigilance._

/** @author Stephen Samuel */
object ComparingUnrelatedTypes extends Inspection {

  override val level = Levels.Error
  override val description = "Comparing unrelated types"

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
     override def traverser = new context.Traverser {

      import context._
      import context.global._

      private def isIntegral(t: Type): Boolean =
        Seq(typeOf[Byte], typeOf[Char], typeOf[Short], typeOf[Int], typeOf[Long]).exists(t <:< _)

      private def areRelated(lt: Type, rt: Type): Boolean =
        lt <:< rt || rt <:< lt || lt =:= rt

      private def isDerivedValueClass(ts: Symbol): Boolean =
        ts.isClass && ts.asClass.isDerivedValueClass

      private def integralLiteralFitsInType(literal: Literal, targetType: Type): Boolean = {
        if (!isIntegral(literal.tpe) || !isIntegral(targetType)) {
          false
        } else {
          // convertTo has built-in range checking and will return null if the value cannot be
          // accurately represented in the target type.
          literal.value.convertTo(targetType) != null
        }
      }


      override def inspect(tree: Tree) = {

          // -- Special cases ---------------------------------------------------------------------

          // Comparing any numeric value to a literal 0 should be ignored:
          case Apply(Select(lhs, Equals), List(Zero)) if lhs.tpe.typeSymbol.isNumericValueClass =>
          case Apply(Select(Zero, Equals), List(rhs)) if rhs.tpe.typeSymbol.isNumericValueClass =>

          // Comparing a integral value to a integral literal should be ignored if the literal
          // fits in the in range of the other value's type. For example, in general it may be
          // unsafe to compare ints to chars because not all ints fit in the range of a char, but
          // such comparisons are safe for small integers: thus `(c: Char) == 97` is a valid
          // comparision but `(c: Char) == 128000` is not.
          case Apply(Select(value, Equals), List(lit: Literal)) if integralLiteralFitsInType(lit, value.tpe) =>
          case Apply(Select(lit: Literal, Equals), List(value)) if integralLiteralFitsInType(lit, value.tpe)  =>

          // -- End special cases ------------------------------------------------------------------

          case Apply(Select(lhs, Equals), List(rhs)) =>
            def eraseIfNecessaryAndCompare(lt: Type, rt: Type): Unit = {
              val symbols = Seq(lt.typeSymbol, rt.typeSymbol)
              val (l, r) = if (symbols.exists(isDerivedValueClass) || symbols.exists(_.isParameter))
                (lt, rt)
              else
                (lt.erasure, rt.erasure)

              if (!areRelated(l, r)) {
                context.warn(tree.pos, self, s"Unrelated types: ${l.toString} & ${r.toString}")
              } else {
                lt.typeArgs.zip(rt.typeArgs).foreach {
                  case (ltInner, rtInner) =>
                    eraseIfNecessaryAndCompare(ltInner, rtInner)
                }
              }
            }

            eraseIfNecessaryAndCompare(lhs.tpe, rhs.tpe)

           }
      }
    }
  }
