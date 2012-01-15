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
    val cacheControl: Option[String] = maxAge(resource).map("max-age=" + _).toSeq match {
      case Seq() => None
      case Seq(clauses @ _*) => Some(clauses.mkString(", "))
    }
    val headers: Seq[(String, String)] = cacheControl.map(("Cache-Control" -> _)).toSeq
    BinaryResponse(mediaType, resource.payload, resource.length, headers: _*)
  }

  def maxAge(resource: Resource): Option[Int] = Some(86400)
}

object ResourceServerController {

  val pathParameter = Parameter[Path]("_errand_path")
}
