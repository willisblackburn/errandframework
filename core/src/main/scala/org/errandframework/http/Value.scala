/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * Base class of the three case classes that represent a value latched in a Parameter.
 * It basically acts like an Either[String, T] with an additional "uninitialized" value.
 */
sealed abstract class Value[+T] {

  import Value.{ ValidProjection, InvalidProjection, UninitializedProjection }

  def isValid = false
  def isInvalid = false
  def isUninitialized = false

  final def valid = ValidProjection(this)

  final def invalid = InvalidProjection(this)

  final def uninitialized = UninitializedProjection(this)

  // Get shortcuts in Value assume validity; use invalid method to project to InvalidProjection if you want to assume
  // invalidity.

  final def get() = valid.get

  final def getOrElse[A >: T](alternative: => A) = valid.getOrElse(alternative)

  final def getOption() = valid.getOption

  final def map[A](f: T => A) = valid.map(f)

  final def flatMap[A](f: T => Value[A]) = valid.flatMap(f)

  final def foreach(f: T => Unit) { valid.foreach(f) }

  final def fold[A](fValid: T => A, fInvalid: String => A, fUninitialized: => A): A = this match {
    case Valid(v) => fValid(v)
    case invalid: Invalid => fInvalid(invalid.message)
    case Uninitialized => fUninitialized
  }
}

object Value {

  final case class ValidProjection[+T](value: Value[T]) {
    def get(): T = value match {
      case Valid(v) => v
      case _: Invalid => throw new NoSuchElementException("Value is Invalid")
      case Uninitialized => throw new NoSuchElementException("Value is Uninitialized")
    }
    def getOrElse[A >: T](alternative: => A) = value match {
      case Valid(v) => v
      case _ => alternative
    }
    def getOption(): Option[T] = value match {
      case Valid(v) => Some(v)
      case _ => None
    }
    // Type variable A in map and flatMap is unconstrained because Invalid and Uninitialized can be assigned to
    // a Valid[A] for any A.  However, for the Invalid and Uninitialized projections, the type must be
    // compatible with the original type A, since those operations don't affect the Valid value.
    def map[A](f: T => A) = value match {
      case Valid(v) => Valid(f(v))
      case invalid: Invalid => invalid
      case Uninitialized => Uninitialized
    }
    def flatMap[A](f: T => Value[A]) = value match {
      case Valid(v) => f(v)
      case invalid: Invalid => invalid
      case Uninitialized => Uninitialized
    }
    def foreach(f: T => Unit) {
      value match {
        case Valid(v) => f(v)
        case _ => ()
      }
    }
  }

  final case class InvalidProjection[+T](value: Value[T]) {
    def map[A](f: String => String) = value match {
      case invalid: Invalid => Invalid(f(invalid.message))
      case other => other
    }
    def flatMap[A >: T](f: String => Value[A]) = value match {
      case invalid: Invalid => f(invalid.message)
      case other => other
    }
    def foreach(f: String => Unit) = value match {
      case invalid: Invalid => f(invalid.message)
      case _ => ()
    }
  }

  final case class UninitializedProjection[+T](value: Value[T]) {
    def flatMap[A >: T](f: => Value[A]) = value match {
      case Uninitialized => f
      case other => other
    }
    def foreach(f: => Unit) = value match {
      case Uninitialized => f
      case _ => ()
    }
  }
}

/**
 * The parameter was read from the request and successfully decoded.
 */
case class Valid[T](value: T) extends Value[T] {
  override def isValid = true
}

/**
 * The parameter was read from the request but could not be decoded.
 */
case class Invalid(message: String) extends Value[Nothing] {
  override def isInvalid = true
}

/**
 * The parameter was read from the request but could not be decoded.
 */
case object Uninitialized extends Value[Nothing] {
  override def isUninitialized = true
}
