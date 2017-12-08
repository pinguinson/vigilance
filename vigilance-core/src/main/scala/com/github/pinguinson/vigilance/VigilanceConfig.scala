package com.github.pinguinson.vigilance

//TODO: add every inspection to com.github.pinguinson.vigilance.inspections.all ?
import com.github.pinguinson.vigilance.inspections._
import com.github.pinguinson.vigilance.inspections.collections._
import com.github.pinguinson.vigilance.inspections.controlflow._
import com.github.pinguinson.vigilance.inspections.empty._
import com.github.pinguinson.vigilance.inspections.equality._
import com.github.pinguinson.vigilance.inspections.exception._
import com.github.pinguinson.vigilance.inspections.imports._
import com.github.pinguinson.vigilance.inspections.inference._
import com.github.pinguinson.vigilance.inspections.matching._
import com.github.pinguinson.vigilance.inspections.math._
import com.github.pinguinson.vigilance.inspections.naming._
import com.github.pinguinson.vigilance.inspections.nulls._
import com.github.pinguinson.vigilance.inspections.option._
import com.github.pinguinson.vigilance.inspections.string._
import com.github.pinguinson.vigilance.inspections.style._
import com.github.pinguinson.vigilance.inspections.unneccesary._
import com.github.pinguinson.vigilance.inspections.unsafe._

/** @author Stephen Samuel */
object VigilanceConfig extends App {

  def inspections: Seq[Inspection] = Seq(
    ArrayEquals,
    ArraysInFormat,
    ArraysToString,
    AvoidOperatorOverload,
    AvoidSizeEqualsZero,
    AvoidSizeNotEqualsZero,
    AvoidToMinusOne,
    BigDecimalDoubleConstructor,
    BigDecimalScaleWithoutRoundingMode,
    BoundedByFinalType,
    BrokenOddness,
    CatchOperations,
    ClassNames,
    CollectionIndexOnNonIndexedSeq,
    CollectionNegativeIndex,
    CollectionPromotionToAny,
    ComparingFloatingPointTypes,
    ComparingUnrelatedTypes,
    ComparisonToEmptyList,
    ComparisonToEmptySet,
    ComparisonWithSelf,
    ConstantIf,
    DivideByOne,
    DoubleNegation,
    DuplicateImport,
    DuplicateMapKey,
    DuplicateSetValue,
    EitherGet,
    EmptyCaseClass,
    EmptyFor,
    EmptyIfBlock,
    EmptyInterpolatedString,
    EmptyMethod,
    EmptySynchronizedBlock,
    EmptyTryBlock,
    EmptyWhileBlock,
    ExistsSimplifiableToContains,
    FilterOperations,
    FinalizerWithoutSuper,
    FinalModifierOnCaseClass,
    FindOperations,
    FutureOptionNames,
    FutureSeqNames,
    IllegalFormatString,
    IncorrectlyNamedExceptions,
    IncorrectNumberOfArgsToFormat,
    InvalidRegex,
    ImpossibleOptionSizeCondition,
    InstanceOf,
    JavaConversionsUse,
    ListAppend,
    ListSize,
    LooksLikeInterpolatedString,
    MapGetAndGetOrElse,
    MethodNames,
    MethodReturningAny,
    ModOne,
    NanComparison,
    NegationIsEmpty,
    NegationNonEmpty,
    NoOpOverride,
    NullAssignment,
    NullParameter,
    ObjectNames,
    OptionGet,
    OptionSize,
    ParameterlessMethodReturnsUnit,
    PartialFunctionInsteadOfMatch,
    PointlessTypeBounds,
    PredefMutableCollection,
    PreferSeqEmpty,
    PreferSetEmpty,
    ProductWithSerializableInferred,
    PublicFinalizer,
    RedundantFinalizer,
    RedundantFinalModifierOnMethod,
    RedundantFinalModifierOnVar,
    RepeatedCaseBody,
    ReverseFunc,
    SimplifyBooleanExpression,
    StripMarginOnRegex,
    SubstringZero,
    SuspiciousMatchOnClassObject,
    SwallowedException,
    SwapSortFilter,
    TraversableHead,
    TraversableLast,
    TryGet,
    TypeShadowing,
    UnnecessaryIf,
    UnnecessaryReturnUse,
    UnnecessaryToInt,
    UnnecessaryToString,
    UnsafeContains,
    UnusedMethodParameter,
    UseCbrt,
    UseExpM1,
    UseLog10,
    UseLog1P,
    UseSqrt,
    VarClosure,
    VarCouldBeVal,
    WhileTrue,
    ZeroNumerator
  )
}
