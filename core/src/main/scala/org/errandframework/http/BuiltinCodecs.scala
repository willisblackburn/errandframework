/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import java.util.UUID

// TODO, additional codecs for:
// 1. Paths (with optional escaping).
// 2. Strings with escaping (for use in the middle of URLs).
// 3. Sets of strings encoded as a comma-delimited list.
// 4. Tags?

/**
 * Collection of built-in codecs.
 * All are implicit objects and so will be picked up by a parameter of the corresponding type.
 */
object BuiltinCodecs {

  import Codec.NO_VALUE

  // TODO, provide a StringCodec that handles null differently.  (Why?)
  // TODO, provide a PathCodec that adds an initial '/' if the string is not blank and doesn't have one already. (Why?)

  // Simple codecs for built-in types:

  implicit object StringCodec extends Codec[String] {

    def encode(value: String) = if (value != null) value else NO_VALUE

    def decode(raw: String) = if (raw != NO_VALUE) raw else null
  }

  /**
   * Encodes a Boolean as either "true" or "false."
   * Note that for form checkboxes you probably want to use BooleanParameter instead of SimpleParameter with
   * BooleanCodec, because form checkboxes do not encode the unchecked state in the request (the unchecked
   * control is not "successful" according to the HTML spec and therefore its value is not submitted to the server),
   * and so using BooleanCodec will produce Invalid.
   */
  implicit object BooleanCodec extends Codec[Boolean] {

    // scala.Boolean cannot be null.

    def encode(value: Boolean) = value.toString

    def decode(raw: String) = try {
      raw.toBoolean
    } catch {
      case e: Exception => throw new DecodeException("Value must be either \"true\" or \"false\": " + raw)
    }
  }

  implicit object IntCodec extends Codec[Int] {

    // scala.Int cannot be null.

    def encode(value: Int) = value.toString

    def decode(raw: String) = try {
      raw.toInt
    } catch {
      case e: Exception => throw new DecodeException("Value must be a number between " + Int.MinValue + " and " + Int.MaxValue + ": " + raw)
    }
  }

  implicit object LongCodec extends Codec[Long] {

    // scala.Long cannot be null.

    def encode(value: Long) = value.toString

    def decode(raw: String) = try {
      raw.toLong
    } catch {
      case e: Exception => throw new DecodeException("Value must be a number between " + Long.MinValue + " and " + Long.MaxValue + ": " + raw)
    }
  }

  implicit object DoubleCodec extends Codec[Double] {

    // scala.Double cannot be null.

    def encode(value: Double) = value.toString

    def decode(raw: String) = try {
      raw.toDouble
    } catch {
      case e: Exception => throw new DecodeException("Value must be a double-precision floating-point number: " + raw)
    }
  }

  // Codecs for other common types.

  implicit object UuidCodec extends Codec[UUID] {

    def encode(value: UUID) = if (value != null) value.toString else NO_VALUE

    def decode(raw: String) = try {
      if (raw != NO_VALUE) UUID.fromString(raw) else null
    } catch {
      case e: Exception => throw new DecodeException("Value must be a UUID in the format aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee: " + raw)
    }
  }

  implicit object ClassCodec extends Codec[Class[_]] {

    def encode(value: Class[_]) = if (value != null) value.getName else NO_VALUE

    def decode(raw: String) = try {
      if (raw != null) Class.forName(raw) else null
    } catch {
      case e: Exception => throw new DecodeException("Class name is malformed, does not exist, or is inaccessible: " + raw)
    }
  }

  implicit object PathCodec extends Codec[Path] {

    def encode(value: Path) = if (value != null) value.toString else NO_VALUE

    def decode(raw: String) = if (raw != NO_VALUE) Path(raw) else null
  }
}

