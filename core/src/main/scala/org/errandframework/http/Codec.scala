/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http


import java.text.{SimpleDateFormat, DateFormat}
import java.util.{UUID, Date}

/**
 * Trait that defines conversions between various data types and a String.
 * Generally implementations of this trait will be objects.
 * Codecs for class instances must be able to handle null references.  It is up to the codec whether it preserves
 * null in those cases.
 * However, neither the value returned from encode or from decode should be null.
 * A desirable property of a codec is that it be symmetrical, that is, decode(encode(x)) should return x.
 * Codecs should .
 */
trait Codec[T] {

  /**
   * Converts the value v into a String.
   */
  def encode(value: T): String

  /**
   * Converts the input String into a value.
   * Also performs any necessary validation.
   */
  def decode(raw: String): T
}

object Codec {

  val NO_VALUE = "<NO_VALUE>"
}

class DateCodec(format: DateFormat) extends Codec[Date] {

  import Codec.NO_VALUE

  def encode(value: Date) = if (value != null) format.format(value) else NO_VALUE

  def decode(raw: String) = try {
    if (raw != NO_VALUE) format.parse(raw) else null
  } catch {
    case e: Exception =>
      // If the DateFormat is SimpleDateFormat, then we can retrieve the pattern and use it in the error.
      // But if it's not SimpleDateFormat then we can't report anything--it might not even use a pattern string.
      val pattern = format match {
        case simpleDateFormat: SimpleDateFormat => simpleDateFormat.toPattern
        case _ => "(unknown)"
      }
      throw new DecodeException("Value does not match the expected date pattern " + pattern + ": " + raw)
  }
}

object DateCodec {

  def apply(format: DateFormat) = new DateCodec(format)

  def apply(formatString: String) = new DateCodec(new SimpleDateFormat(formatString))
}

/**
 * OptionCodec prefixes the encoding of another codec with "+" if the input value was wrapped in Some,
 * otherwise it encodes "-".  When decoding, if the value starts with "+", then OptionCodec passes the remainder
 * of the string to the other codec and returns the result wrapped in Some, otherwise returns None.
 * Use Parameter with OptionCodec when using a parameter in the form, particularly for mapping radio buttons
 * and select list options to Some(x) and None.
 * To implement optional page parameters, use OptionParameter, which decodes to None if the parameter is not
 * present in the request.
 */
class OptionCodec[T](codec: Codec[T]) extends Codec[Option[T]] {

  import Codec.NO_VALUE

  def encode(value: Option[T]) = if (value != null) value.map("+" + codec.encode(_)).getOrElse("-") else NO_VALUE

  def decode(raw: String) = if (raw != NO_VALUE) {
    raw.headOption match {
      case Some('+') => Some(codec.decode(raw.substring(1)))
      case Some('-') => None
      case _ => throw new DecodeException("Value does not begin with Some (\"+\") or None (\"-\") character: " + raw)
    }
  } else null
}

object OptionCodec {

  def apply[T](implicit codec: Codec[T]) = new OptionCodec(codec)
}

