/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import org.errandframework.util.Log

/**
 * BooleanParameter represents a parameter that is either true or false.  If the parameter is present in the request,
 * then the parameter is true, otherwise, it is false.  
 * The actual value of the parameter in the request is not considered.
 */
class BooleanParameter(val name: String) extends RequestParameter[Boolean] {
  
  import BooleanParameter.log

  def raw = RequestContext.request.getParameter(name)

  def decode() = {
    val decoded = Valid(raw.isDefined)
    log.debug("Decoded BooleanParameter: " + name + ": " + raw + " -> " + decoded)
    decoded
  }

  def encode(value: Boolean) = {
    val encoded = if (value) Seq(name) else Seq.empty
    log.debug("Encoded Parameter: " + name + ": " + value + " -> " + encoded)
    encoded
  }

  def encodeAsString(value: Boolean) =
    throw new UnsupportedOperationException("BooleanParameter does not support encodeAsString")

  override def toString() = "BooleanParameter(name=" + name + ")"
}

object BooleanParameter extends Log {

  /**
   * Allow the creation of a BooleanParameter without requiring "new."
   */
  def apply(): BooleanParameter = apply(RequestParameter.newName)

  def apply(name: String) = new BooleanParameter(name)
}
