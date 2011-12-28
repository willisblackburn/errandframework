package org.errandframework

import java.util.UUID

package object http {

  /**
   * The root location, used (directly or indirectly) to create all other locations.
   */
  val rootLocation = new Location(Nil)

  val rootPath = new Path(Nil)

  import Codec.NO_VALUE

  implicit val stringCodec = new Codec[String] {

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
  implicit val booleanCodec = new Codec[Boolean] {

    // scala.Boolean cannot be null.

    def encode(value: Boolean) = value.toString

    def decode(raw: String) = try {
      raw.toBoolean
    } catch {
      case e: Exception => throw new DecodeException("Value must be either \"true\" or \"false\": " + raw)
    }
  }

  implicit val intCodec = new Codec[Int] {

    // scala.Int cannot be null.

    def encode(value: Int) = value.toString

    def decode(raw: String) = try {
      raw.toInt
    } catch {
      case e: Exception => throw new DecodeException("Value must be a number between " + Int.MinValue + " and " + Int.MaxValue + ": " + raw)
    }
  }

  implicit val longCodec = new Codec[Long] {

    // scala.Long cannot be null.

    def encode(value: Long) = value.toString

    def decode(raw: String) = try {
      raw.toLong
    } catch {
      case e: Exception => throw new DecodeException("Value must be a number between " + Long.MinValue + " and " + Long.MaxValue + ": " + raw)
    }
  }

  implicit val doubleCodec = new Codec[Double] {

    // scala.Double cannot be null.

    def encode(value: Double) = value.toString

    def decode(raw: String) = try {
      raw.toDouble
    } catch {
      case e: Exception => throw new DecodeException("Value must be a double-precision floating-point number: " + raw)
    }
  }

  // Codecs for other common types.

  implicit val uuidCodec = new Codec[UUID] {

    def encode(value: UUID) = if (value != null) value.toString else NO_VALUE

    def decode(raw: String) = try {
      if (raw != NO_VALUE) UUID.fromString(raw) else null
    } catch {
      case e: Exception => throw new DecodeException("Value must be a UUID in the format aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee: " + raw)
    }
  }

  implicit val classCodec = new Codec[Class[_]] {

    def encode(value: Class[_]) = if (value != null) value.getName else NO_VALUE

    def decode(raw: String) = try {
      if (raw != null) Class.forName(raw) else null
    } catch {
      case e: Exception => throw new DecodeException("Class name is malformed, does not exist, or is inaccessible: " + raw)
    }
  }

  implicit val pathCodec = new Codec[Path] {

    def encode(value: Path) = if (value != null) value.toString else NO_VALUE

    def decode(raw: String) = if (raw != NO_VALUE) Path(raw) else null
  }
}