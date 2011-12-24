/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import org.jclouds.io.Payload
import org.jclouds.io.payloads.InputStreamPayload

/**
 * ClassPathController loads a static resource from the classpath.
 */
class ClassPathResourceFinder(mediaTypeMapper: MediaTypeMapper) extends ResourceFinder {

  def get(path: Path) = {
    ClassPathHelpers.getResourceAsStream(path) match {
      case Some(stream) =>
        val payload: Payload = new InputStreamPayload(stream)
        payload.getContentMetadata.setContentType(mediaTypeMapper.getMediaTypeForExtension(path.extension).toString)
        payload
      case _ => throw new ResourceNotFoundException
    }
  }
}
