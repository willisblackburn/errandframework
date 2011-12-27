package org.errandframework.http

/**
 * Subclass of ErrandServlet that provides some common mixins and settings.
 */
class DefaultErrandServlet extends ErrandServlet with DynamicControllerProvider with ResourceServerControllerProvider {

  protected val mediaTypeMapper = new StaticMediaTypeMapper(
    MediaType("image", "jpeg") -> Seq("jpg", "jpeg"),
    MediaType("image", "png") -> Seq("png"),
    MediaType("image", "gif") -> Seq("gif"),
    MediaType("text", "html") -> Seq("html", "htm"),
    MediaType("text", "css") -> Seq("css"),
    MediaType("text", "javascript") -> Seq("js")
  )

  val dynamicLocation = rootLocation / "dyn"
  protected val dynamicController = new DynamicController

  val resourceServerLocation = rootLocation / "res"
  protected val resourceServerController = new ResourceServerController(new ClassPathResourceFinder(mediaTypeMapper))

  private val defaultMapping: PartialFunction[Path, Controller] = {
    case rootLocation() => XhtmlResponse(
      <html xmlns="http://www.w3.org/1999/xhtml">
        <h1>Welcome to Errand</h1>
        <p>
          You are seeing this page because you have not created a mapping for the root location
          in your Errand servlet.
        </p>
      </html>)
    case dynamicLocation() => dynamicController
    case resourceServerLocation() => resourceServerController
  }

  protected def mappers = Seq(new PathRequestMapper(defaultMapping))
}
