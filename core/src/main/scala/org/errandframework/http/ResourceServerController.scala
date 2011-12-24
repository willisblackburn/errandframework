/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import BuiltinCodecs.PathCodec
import MediaType._

/**
 * ResourceServerController serves a static resource.
 * The resource may come from any ResourceFinder implementation.
 */
class ResourceServerController(resourceFinder: ResourceFinder) extends Controller with ParameterProcessor {

  import ResourceServerController._

  def parameters = Seq(pathParameter)

  def onValid(request: Request) = {
    // TODO, map extension to content type.  Use servlet API?
    // TODO, content type, length, etc.
    val payload = resourceFinder.get(pathParameter.get)
    val metadata = payload.getContentMetadata
    val mediaType = Option(metadata.getContentType).getOrElse("application/octet-stream")
    val length = Option(metadata.getContentLength).map(_.toInt)
    BinaryResponse(mediaType, payload.getInput, length)
  }
}

object ResourceServerController {

  val pathParameter = Parameter[Path]("_errand_path")
}
