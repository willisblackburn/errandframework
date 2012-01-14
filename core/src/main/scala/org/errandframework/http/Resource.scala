package org.errandframework.http

import java.util.Date
import java.io.InputStream

/**
 * A Resource is a payload (content or data) combined with metadata.
 */
trait Resource {

  /**
   * Returns an InputStream from which the caller may retrieve the resource payload.
   * Multiple calls will return the same InputStream.
   */
  def payload: InputStream

  def mediaType: Option[MediaType] = None

  def length: Option[Long] = None

  def lastModifiedTime: Option[Date] = None

  def cacheControl: Option[String] = None

  def maxAge: Option[Int] = None
}
