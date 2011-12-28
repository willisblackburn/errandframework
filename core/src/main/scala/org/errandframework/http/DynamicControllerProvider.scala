package org.errandframework.http

/**
 * Mixin trait for ErrandServlet that provides a dynamic controller.
 * Needed for most Errand features, such as generating dynamic links (for example, ActionLink) and returning
 * PageResponse from POST requests.
 * Provides defaults for
 */
trait DynamicControllerProvider {

  this: ErrandServlet =>

  val dynamicLocation: Location
  protected val dynamicController: DynamicController

  /**
   * Registers a new dynamic controller and returns the URL.
   * The returned URL will be context-relative, because sometimes we use it for forwarding a request internally.
   * If the URL will be sent to the browser, then the context path (RequestContext.request.contextServletPath) should be
   * prepended.
   */
  def registerController(controller: Controller) = dynamicController.register(controller)

  def urlForControllerId(controllerId: Int) = dynamicLocation.toUrl(DynamicController.controllerIdParameter -> controllerId)

  def localUrlForControllerId(controllerId: Int) = dynamicLocation.toLocalUrl(DynamicController.controllerIdParameter -> controllerId)
}

object DynamicControllerProvider {

  def get(): DynamicControllerProvider = RequestContext.servlet match {
    case provider: DynamicControllerProvider => provider
    case _ => throw new UnsupportedOperationException("Servlet does not mix in DynamicControllerProvider")
  }

  def registerController(controller: Controller) = get.registerController(controller)

  def urlForControllerId(controllerId: Int) = get.urlForControllerId(controllerId)

  def localUrlForControllerId(controllerId: Int) = get.localUrlForControllerId(controllerId)
}