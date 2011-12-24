/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import org.errandframework.util.Log
import java.net.URLEncoder

/**
 * MultiParameter is similar to Parameter except that it decodes multiple parameters with the same name in the
 * request into a single List.  If there are no parameters in the request with this name, MultiParameter produces
 * an empty list.
 */
class MultiParameter[T](val name: String, codec: Codec[T]) extends RequestParameter[Seq[T]] {

  import MultiParameter.log

  def raw = RequestContext.request.getParameters(name)

  /**
   * Decodes the parameter value from the request using this parameters codec and validates it.
   * @return a Value instance containing the (Valid) decoded value or the (Invalid) raw value.
   */
  def decode() = {
    val decoded = try {
      Valid(raw.map(codec.decode))
    } catch {
      case e: Exception => Invalid(formatErrorMessage("exception")(name, e.getClass.getSimpleName, e.getMessage))
    }
    log.debug("Decoded Parameter: " + name + ": " + raw + " -> " + decoded)
    decoded
  }

  def encode(value: Seq[T]) = {
    val encoded = value.map(v => name + "=" + URLEncoder.encode(codec.encode(v), "UTF-8"))
    log.debug("Encoded MultiParameter: " + name + ": " + value + " -> " + encoded)
    encoded
  }

  /**
   * Encodes one value of type T as a single string, without the parameter name.
   * Value is NOT URL-encoded.
   */
  def encodeAsString(value: T): String = {
    val encoded = codec.encode(value)
    log.debug("Encoded MultiParameter value: " + name + ": " + value + " -> " + encoded)
    encoded
  }

  override def toString() = "MultiParameter(name=" + name + ")"
}

object MultiParameter extends Log {

  /**
   * Allow the creation of a Parameter without requiring "new."
   */
  def apply[T]()(implicit codec: Codec[T]): MultiParameter[T] = apply(RequestParameter.newName)

  def apply[T](name: String)(implicit codec: Codec[T]) = new MultiParameter[T](name, codec)
}


