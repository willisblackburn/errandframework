package org.errandframework.http

/**
 * Mixin trait for ErrandServlet that provides a resource server controller.
 * Needed for most Errand features, such as generating dynamic links (for example, ActionLink) and returning
 * PageResponse from POST requests.
 */
trait ResourceServerControllerProvider {

  this: ErrandServlet =>

  protected val mediaTypeMapper: MediaTypeMapper

  val resourceServerLocation: Location
  protected val resourceServerController: ResourceServerController

  def urlForResource(path: Path) = resourceServerLocation.toUrl(ResourceServerController.pathParameter -> path)
}

object ResourceServerControllerProvider {

  def get(): ResourceServerControllerProvider = RequestContext.servlet match {
    case provider: ResourceServerControllerProvider => provider
    case _ => throw new UnsupportedOperationException("Servlet does not mix in ResourceServerControllerProvider")
  }

  def urlForResource(path: Path) = get.urlForResource(path)
}