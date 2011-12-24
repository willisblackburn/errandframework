/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * Thrown when a parameter, field, or form fails to validate.
 * Throwing this exception does not in itself does not display the message to the user.
 * The message is added to the messages list after the exception is caught.
 * The included Throwable is for diagnostic purposes.
 */
class DecodeException(message: String, cause: Throwable) extends RuntimeException(message, cause) {

  def this(key: String) {
    this(key, null)
  }
}
