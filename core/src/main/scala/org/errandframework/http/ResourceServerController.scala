/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * ResourceServerController serves a static resource.
 * The resource may come from any ResourceFinder implementation.
 */
class ResourceServerController(resourceFinder: ResourceFinder) extends Controller with ParameterProcessor {

  import ResourceServerController._

  def parameters = Seq(pathParameter)

  def onValid(request: Request) = {
    val resource = resourceFinder.get(pathParameter.get)
    val mediaType = resource.mediaType.getOrElse(MediaType("application", "octet-stream"))
    val cacheControl = (resource.cacheControl ++ resource.maxAge.map("max-age=" + _)).mkString(", ")
    val headers: Seq[(String, String)] = if (cacheControl != "") Seq(("Cache-Control" -> cacheControl)) else Seq.empty
    BinaryResponse(mediaType, resource.payload, resource.length, headers: _*)
  }
}

object ResourceServerController {

  val pathParameter = Parameter[Path]("_errand_path")
}
