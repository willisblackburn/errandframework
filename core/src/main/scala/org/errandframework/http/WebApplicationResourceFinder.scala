/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * WebApplicationResource loads a static resource from the web application path.
 */
class WebApplicationResourceFinder(mediaTypeMapper: MediaTypeMapper) extends ResourceFinder {

  def get(path: Path) = {
    RequestContext.application.getResourceAsStream(path) match {
      case Some(stream) =>
        new Resource {
          val payload = stream
          override val mediaType = Some(mediaTypeMapper.getMediaTypeForExtension(path.extension))
          override val length = Some(stream.available.toLong)
        }
      case _ => throw new ResourceNotFoundException
    }
  }
}
