/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.util

/**
 * Utility methods for handling exceptions.
 */
object ExceptionHelpers {

  def tryOption[T](f: => T): Option[T] = try {
    Some(f)
  } catch {
    case _ => None
  }

  def tryOr[T](f: => T)(alternative: => T): T = try {
    f
  } catch {
    case _ => alternative
  }
}