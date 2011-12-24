/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

import org.errandframework.util.Log
import java.net.URLEncoder

/**
 * OptionParameter is similar to Parameter except that it decodes to None if the parameter is not present
 * in the request.
 */
class OptionParameter[T](val name: String, codec: Codec[T]) extends RequestParameter[Option[T]] {

  import OptionParameter.log

  def raw = RequestContext.request.getParameter(name)

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

  def encode(value: Option[T]) = {
    val encoded = value.map(v => name + "=" + URLEncoder.encode(codec.encode(v), "UTF-8")).toSeq
    log.debug("Encoded OptionParameter: " + name + ": " + value + " -> " + encoded)
    encoded
  }

  override def toString() = "OptionParameter(name=" + name + ")"
}

object OptionParameter extends Log {

  /**
   * Allow the creation of a Parameter without requiring "new."
   */
  def apply[T]()(implicit codec: Codec[T]): OptionParameter[T] = apply(RequestParameter.newName)

  def apply[T](name: String)(implicit codec: Codec[T]) = new OptionParameter[T](name, codec)
}


