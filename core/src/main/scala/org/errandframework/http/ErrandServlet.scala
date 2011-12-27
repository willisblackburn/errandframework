/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import org.errandframework.util.Log

/**
 * Main Errand servlet.
 */
abstract class ErrandServlet extends HttpServlet with Log {

  log.info("Creating ErrandServlet")

  protected def filters: Seq[RequestFilter] = Seq.empty

  protected def mappers: Seq[RequestMapper]

  override def init() {
    log.info("Initializing ErrandServlet")
  }

  override def destroy() {
    log.info("Destroying ErrandServlet")
  }

  override def service(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {

    val requestContext = createRequestContext(httpRequest, httpResponse)

    log.info("Servicing request: " + requestContext.request)
    val startMillis = System.currentTimeMillis

    RequestContext.withValue(Some(requestContext)) {

      val response = try {
        invokeFilters(requestContext.request, filters)
      } catch {
        case e: Exception => dispatchException(requestContext.request, e)
      }

      log.info("Sending response: " + response)

      response.send(httpRequest, httpResponse)
    }

    val millis = System.currentTimeMillis - startMillis
    log.info("Completed request in " + millis + " ms: " + requestContext.request)
  }

  protected def createRequestContext(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) =
    new RequestContext(httpRequest, httpResponse, this)

  private def invokeFilters(request: Request, filters: Seq[RequestFilter]): Response = {
    filters.headOption match {
      case Some(filter) => filter.filter(request)(invokeFilters(_, filters.tail))
      case _ => dispatchRequest(request)
    }
  }

  protected def dispatchRequest(request: Request): Response = {
    val controllers = resolveControllers(request)
    val controller = controllers.headOption.getOrElse(throw new NoControllerException)
    controller.respond(request)
  }

  private def resolveControllers(request: Request): Seq[Controller] = {
    val controllers = mappers.flatMap(_.resolve(request))
    // In each tuple, _1 is the controller, _2 is the score.  Sort higher scores first.
    controllers.sortWith((r1, r2) => r1._2 > r2._2).map(_._1)
  }

  protected def dispatchException(request: Request, exception: Exception): Response = ExceptionResponse(exception)

  def resourceBundleNames: List[String] = ClassPathHelpers.expandSuperclasses(getClass).map(_.getName)
}

object ErrandServlet {

  def get() = RequestContext.servlet
}

