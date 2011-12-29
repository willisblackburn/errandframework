/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package pages

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.errandframework.http._
import org.errandframework.util.Log
import DynamicControllerProvider.{urlForControllerId, registerController}

/**
 * Respond to the request with a page.
 */
class PageResponse(page: Page) extends Response {

  import PageResponse.log

  def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {

    val xhtml = Page.withValue(Some(page))(page.render)

    // We've rendered the page now.
    // If the browser requested this page with a GET, then send it directly.
    // If the browser sent anything else, including POST, create a new dynamic controller that will return the page,
    // and redirect the browser to it.  This requires that the servlet have the DynamicControllerProvider mixin.

    val response = XhtmlResponse(xhtml)
    val responseToSend = httpRequest.getMethod match {
      case "GET" =>
        log.debug("Sending page in response to GET")
        response
      case method =>
        val controller = new Controller {
          def respond(request: Request) = XhtmlResponse(xhtml)
        }
        val controllerId = registerController(controller)
        val url = urlForControllerId(controllerId)
        log.debug("Redirecting to " + url + " in response to " + method)
        RedirectResponse(url)
    }
    responseToSend.send(httpRequest, httpResponse)
  }
}

object PageResponse extends Log {

  def apply(page: Page) = new PageResponse(page)
}