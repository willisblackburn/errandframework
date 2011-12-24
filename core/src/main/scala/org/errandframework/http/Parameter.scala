/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import org.errandframework.util.Log
import java.net.URLEncoder

// Parameter error scheme:
// parameter.error = Top-level "something went wrong" error:  0 = parameter name
// parameter.error.missing.{name} = Parameter value not supplied
// parameter.error.exception.{name} = Encountered exception while decoding:  1 = exception type, 2 = exception message
// parameter.error.short.{name} = Input value was too short:  1 = minimum length
// parameter.error.long.{name} = Input value was too long:  1 = maximum length

/**
 * Parameter is a type of RequestParameter that translates between the type T and a simple string value.
 * A Parameter always maps to exactly one string value in the request with the same name as the parameter.
 * If the parameter is not present in the request,
 * then Parameter returns the value Invalid.
 * If there is more than one value for the parameter in the request, then Parameter uses the first one.
 * (Use BooleanParameter, OptionParameter, or ListParameter if you need to different behavior for the
 * no-value and many-values cases.)
 */
class Parameter[T](val name: String, codec: Codec[T]) extends SingleValuedRequestParameter[T] {

  import Parameter.log

  def raw = RequestContext.request.getParameter(name)

  /**
   * Decodes the parameter value from the request using this parameters codec and validates it.
   * @return a Value instance containing the (Valid) decoded value or the (Invalid) raw value.
   */
  def decode() = {
    val decoded = raw.map(decodeRaw).getOrElse(Invalid(formatErrorMessage("missing")(name)))
    log.debug("Decoded Parameter: " + name + ": " + raw + " -> " + decoded)
    decoded
  }

  /**
   * Decode a value from a single String.
   * The raw value should NOT be URL-encoded.
   */
  def decodeRaw(raw: String): Value[T] = try {
    Valid(codec.decode(raw))
  } catch {
    case e: Exception => Invalid(formatErrorMessage("exception")(name, e.getClass.getSimpleName, e.getMessage))
  }

  final def decodeRawAndSet(raw: String): Value[T] = value.uninitialized flatMap {
    val value = decodeRaw(raw)
    _decodedValue.value = value
    value
  }

  def encode(value: T) = {
    val encoded = Seq(name + "=" + URLEncoder.encode(codec.encode(value), "UTF-8"))
    log.debug("Encoded Parameter: " + name + ": " + value + " -> " + encoded)
    encoded
  }

  def encodeAsString(value: T) = {
    val encoded = codec.encode(value)
    log.debug("Encoded Parameter value: " + name + ": " + value + " -> " + encoded)
    encoded
  }

  override def toString() = "Parameter(name=" + name + ")"
}

object Parameter extends Log {

  /**
   * Allow the creation of a Parameter without requiring "new."
   */
  def apply[T]()(implicit codec: Codec[T]): Parameter[T] = apply(RequestParameter.newName)

  def apply[T](name: String)(implicit codec: Codec[T]) = new Parameter[T](name, codec)
}


