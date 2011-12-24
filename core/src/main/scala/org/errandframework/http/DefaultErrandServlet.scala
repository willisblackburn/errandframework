package org.errandframework.http

/**
 * Subclass of ErrandServlet that provides some common mixins and settings.
 */
abstract class DefaultErrandServlet extends ErrandServlet with DynamicControllerProvider with ResourceServerControllerProvider {

  protected val mediaTypeMapper = new StaticMediaTypeMapper(
    MediaType("image", "jpeg") -> Seq("jpg", "jpeg"),
    MediaType("image", "png") -> Seq("png"),
    MediaType("image", "gif") -> Seq("gif"),
    MediaType("text", "html") -> Seq("html", "htm"),
    MediaType("text", "css") -> Seq("css"),
    MediaType("text", "javascript") -> Seq("js")
  )

  val dynamicLocation = Location.rootLocation / "dyn"
  protected val dynamicController = new DynamicController

  val resourceServerLocation = Location.rootLocation / "res"
  protected val resourceServerController = new ResourceServerController(new ClassPathResourceFinder(mediaTypeMapper))
}