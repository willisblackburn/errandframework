/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * Interface for classes that translate between MediaType instances and extensions.
 */
trait MediaTypeMapper {

  def getExtension(mediaType: MediaType): Option[String]

  def getMediaTypeForExtension(extension: Option[String]): MediaType
}

