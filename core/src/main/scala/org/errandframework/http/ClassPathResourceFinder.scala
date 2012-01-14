/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * ClassPathController loads a static resource from the classpath.
 */
class ClassPathResourceFinder(mediaTypeMapper: MediaTypeMapper) extends ResourceFinder {

  def get(path: Path) = {
    ClassPathHelpers.getResourceAsStream(path) match {
      case Some(stream) =>
        new Resource {
          val payload = stream
          override val mediaType = Some(mediaTypeMapper.getMediaTypeForExtension(path.extension))
          override val length = Some(stream.available.toLong)
          override def cacheControl = Some("public")
          override def maxAge = Some(86400)
        }
      case _ => throw new ResourceNotFoundException
    }
  }
}
