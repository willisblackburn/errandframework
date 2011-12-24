/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

// TODO, maybe just move this method to Parameter.

/**
 * SingleValuedRequestParameter identifies parameters that can be consistently encoded as a single string.
 * Parameters that can be encoded a single string may be embedded in URLs or used as a field value,
 * whereas parameters that potentially
 * encode as no parameter, or as multiple parameters, or as a non-string value, may not.
 */
trait SingleValuedRequestParameter[T] extends RequestParameter[T] {

  /**
   * Encodes the given value as a single string, without the parameter name.
   * Value is NOT URL-encoded.
   */
  def encodeAsString(value: T): String
}
