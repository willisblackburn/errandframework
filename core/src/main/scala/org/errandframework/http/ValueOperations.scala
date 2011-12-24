/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * Mixin that provide some additional operations for any class that a value property.
 */
trait ValueOperations[T] {

  def value: Value[T]

  /**
   * Retrieves the value, provided that it is valid.
   * Throws an exception if the parameter value is not Valid, so only call if you've
   * verified that the parameter value is Valid first.
   */
  final def get() = value.get

  /**
   * Retrieves the value, if it is valid, or returns an alternative value.
   */
  final def getOrElse(alternative: => T) = value.getOrElse(alternative)

  /**
   * Retrieves the value as an Option.
   * @return Some(value) if the value is valid, otherwise None
   */
  final def getOption() = value.getOption

  final def map[A](f: T => A) = value.map(f)

  final def flatMap[A](f: T => Value[A]) = value.flatMap(f)

  final def foreach(f: T => Unit) { value.foreach(f) }

  final def fold[A](fValid: T => A, fInvalid: String => A, fUninitialized: => A): A = value.fold(fValid, fInvalid, fUninitialized)
}