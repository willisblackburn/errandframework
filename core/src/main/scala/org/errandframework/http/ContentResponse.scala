/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import HttpServletResponse.{SC_OK, SC_INTERNAL_SERVER_ERROR}
import java.io.{InputStream, Reader}
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import org.apache.commons.io.IOUtils

/**
 * Provides an implementation of the Response.send method that sets the status and media type.
 */
trait ContentResponse extends Response {

  val status = SC_OK
  // TODO, add optional length, name, encoding.
  val mediaType: MediaType
  val length: Option[Int] = None
  val headers: Seq[(String, String)] = Seq.empty

  def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {
    httpResponse.setStatus(status)
    httpResponse.setContentType(mediaType.toString)
    length.foreach(httpResponse.setContentLength)
    headers.foreach(h => httpResponse.setHeader(h._1, h._2))
  }
}

/**
 * HeadResponse is a response that doesn't send any body.
 */
class HeadResponse(val mediaType: MediaType, override val length: Option[Int], override val headers: (String, String)*)
  extends ContentResponse

object HeadResponse {

  def apply(mediaType: MediaType, length: Option[Int], headers: (String, String)*) =
    new HeadResponse(mediaType, length, headers: _*)
}

class CharacterResponse(val mediaType: MediaType, reader: Reader, override val length: Option[Int], override val headers: (String, String)*)
  extends ContentResponse {

  // TODO, should return length in bytes not characters.  Probably I should read into a buffer, then send BinaryBufferResponse.
  // Or send in chunked mode?

  // TODO, what to do about content encoding?  The reader will have one--should the writer use it?

  override def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {
    super.send(httpRequest, httpResponse)
    IOUtils.copy(reader, httpResponse.getWriter)
  }
}

object CharacterResponse {

  def apply(mediaType: MediaType, reader: Reader, length: Option[Int], headers: (String, String)*) =
    new CharacterResponse(mediaType, reader, length, headers: _*)
}

class CharacterBufferResponse(val mediaType: MediaType, s: CharSequence, override val headers: (String, String)*)
  extends ContentResponse {

  // TODO, should return length in bytes not characters.  Probably I should read into a buffer, then send BinaryBufferResponse.
  override val length = Some(s.length)

  override def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {
    super.send(httpRequest, httpResponse)
    httpResponse.getWriter.write(s.toString)
  }
}

object CharacterBufferResponse {

  def apply(mediaType: MediaType, s: CharSequence, headers: (String, String)*) =
    new CharacterBufferResponse(mediaType, s, headers: _*)
}

/**
 * BinaryResponse is a response consisting of an arbitrary length of bytes.
 */
class BinaryResponse(val mediaType: MediaType, stream: InputStream, override val length: Option[Int], override val headers: (String, String)*)
  extends ContentResponse {

  override def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {
    super.send(httpRequest, httpResponse)
    IOUtils.copy(stream, httpResponse.getOutputStream)
  }
}

object BinaryResponse {

  def apply(mediaType: MediaType, stream: InputStream, length: Option[Int], headers: (String, String)*) =
    new BinaryResponse(mediaType, stream, length, headers: _*)
}

class BinaryBufferResponse(val mediaType: MediaType, bytes: Array[Byte], override val headers: (String, String)*)
  extends ContentResponse {

  override val length = Some(bytes.length)

  override def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {
    super.send(httpRequest, httpResponse)
    httpResponse.getOutputStream.write(bytes)
  }
}

object BinaryBufferResponse {

  def apply(mediaType: MediaType, bytes: Array[Byte], length: Option[Int], headers: (String, String)*) =
    new BinaryBufferResponse(mediaType, bytes, headers: _*)
}

class ExceptionResponse(exception: Exception) extends ContentResponse {

  override val status = SC_INTERNAL_SERVER_ERROR
  override val mediaType = MediaType("text", "plain")

  override def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {
    super.send(httpRequest, httpResponse)
    val writer = httpResponse.getWriter
    writer.println("Internal error: " + exception.getMessage)
    exception.printStackTrace(writer)
  }
}

object ExceptionResponse {

  def apply(exception: Exception) = new ExceptionResponse(exception)
}

class ImageResponse(image: BufferedImage) extends ContentResponse {

  val mediaType = MediaType("image", "png")

  override def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {
    super.send(httpRequest, httpResponse)
    ImageIO.write(image, "png", httpResponse.getOutputStream)
  }
}

object ImageResponse {

  def apply(image: BufferedImage) = new ImageResponse(image)
}
