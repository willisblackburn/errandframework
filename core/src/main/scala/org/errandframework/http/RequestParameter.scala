/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import java.util.UUID
import org.errandframework.components.MessageResolver

/**
 * <p>RequestParameter is a typed translation between an object of some type T and a map in which the keys are strings
 * and the values are sequences of strings (in other words a Map[String, Seq[String]]).
 * The map representation maps well to the way that parameters are represented in URLs and HTTP requests.
 * Implementations are responsible for providing the decode method, which retrieves the value from the request
 * and decodes it, and the encode method, which performs the opposite function.</p>
 * <p>RequestParameter has subclasses that must be used in certain contexts.  In general the parameter subclasses
 * applicable to each context depends on how the parameter value will be encoded.  In most cases applications will
 * use the Parameter subclass, which guarantees that the parameter value can be encoded in a single string
 * identified by the parameter name, whereas RequestParameter may encode to several string values with different
 * names.</p>
 * <ul>
 * <li>Binding to a form input field, button, or textarea:  Requires a Parameter because we cannot encode
 * multiple strings in a single form field.</li>
 * <li>Binding to a form checkbox:  Requires BooleanParameter.</li>
 * <li>Binding to a radio button:  Requires a Parameter because we must represent each possible value as a single string.</li>
 * <li>Binding to a select list:  Requires a Parameter because we must represent each possible value as a single string.</li>
 * <li>Binding to a multiselect list:  Requires a ListParameter, which uses a single string for each value.</li>
 * <li>Retrieving a value from the request:  May use any RequestParameter implementation.</li>
 * <li>Using a parameter as part of a Location:  Requires a Parameter because each of the path segments is an unnamed
 * single string.  Note that the encoded value for the parameter may include path separators ('/');  these will only
 * work properly if the parameter is the the last segment of the Location.</li>
 * <li>Adding parameters to a request via the URL or a URL-encoded request body:  May use any RequestParameter
 * implementation will work because both encodings allow the requester to specify both names and values, and
 * multiple values for a single name.
 * </ul>
 * <p>Parameters are typically used in pages and in forms.</p>
 * <p>Page parameters are usually defined as part of the Page subclass to which they apply, and are usually
 * defined public so that callers can use them in links (although this is not required).  PageController automatically
 * decodes values for any parameters that it finds in the parameters list.</p>
 * <p>Form parameters are defined just like page parameters, but usually as part of the Form component that contains
 * them, and usually private because application typically do not link directly to form controllers. The controller
 * embedded in DynamicControllerForm automatically decodes any parameters that it finds in the form's
 * parameters list.</p>
 */
trait RequestParameter[T] extends ValueOperations[T] {

  def name: String

  protected val _decodedValue = RequestVar[Value[T]](Uninitialized)

  def value = _decodedValue.value

  /**
   * Decode the first value of this parameter.
   * Suitable if this parameter maps to only one value in the application.
   */
  def decode(): Value[T]

  final def decodeAndSet(): Value[T] = value.uninitialized flatMap {
    // Decode if it's uninitialized, otherwise just keep the existing value.
    val value = decode
    _decodedValue.value = value
    value
  }

  /**
   * Provides a full encoding of this parameter, suitable for inclusion in a query string or a POST request body.
   * Values will be URL-encoded and multiple parameters separated by "&".
   */
  def encode(value: T): Seq[String]

  def formatErrorMessage(key: String) =
    MessageResolver.formatMessage("org.errandframework.http.RequestParameter", "parameter.error." + key + "." + name) _

  override def equals(that: Any) = that match {
    case parameter: RequestParameter[_] => name == parameter.name
    case _ => false
  }

  override def hashCode() = name.hashCode

  def toAssignment() = new ParameterAssignment[T](this, get)

  def toAssignment(value: T) = new ParameterAssignment[T](this, value)
}

object RequestParameter {

  def newName = UUID.randomUUID.toString
}