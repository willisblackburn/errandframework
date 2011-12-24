/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import HttpServletResponse.SC_NOT_ACCEPTABLE

/**
 * A response from a controller.
 */
trait Response {

  def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse)
}

class StatusResponse(status: Int, headers: (String, String)*) extends Response {

  def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {
    httpResponse.setStatus(status)
    httpResponse.getWriter.println("Status = " + status)
  }
}

object StatusResponse {

  def apply(status: Int, headers: (String, String)*) = new StatusResponse(status, headers: _*)
}

/**
 * ForwardResponse instructs the framework to use the forward method of javax.servlet.RequestDispatcher to
 * forward the request to a new internal path.
 * Note that ForwardResponse accepts a <i>path</i> while RedirectResponse accepts a <i>URL</i>.
 * @path the path to which the request should be directed.  Will be passed through R.encodeUrl.
 */
class ForwardResponse(path: String) extends Response {

  def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {
    val dispatcher = httpRequest.getRequestDispatcher(path)
    dispatcher.forward(httpRequest, httpResponse)
  }
}

object ForwardResponse {

  def apply(path: String) = new ForwardResponse(path)
}

/**
 * RedirectResponse sends an HTTP response code 302 instructing the browser to request a document from
 * a different location.
 * Note that ForwardResponse accepts a <i>path</i> while RedirectResponse accepts a <i>URL</i>.
 * It's okay to provide a path to RedirectResponse, however, because the servlet container is required to convert
 * it to a URL before sending it to the client.
 * @path the path to which the request should be directed.  Will be passed through R.encodeRedirectURL.
 */
class RedirectResponse(url: String) extends Response {

  def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {
    httpResponse.sendRedirect(httpResponse.encodeRedirectURL(url))
  }
}

object RedirectResponse {

  def apply(url: String) = new RedirectResponse(url)
}

/**
 * NegotiatedResponse allows the application to vary its response depending on the media types that the
 * browser can accept.
 * It provides an implementation of Response.send that generates a new response (via responseForMediaType) that
 * can vary depending on the negotiated media type.  NegotiatedResponse then sends the new response.
 */
trait NegotiatedResponse extends Response {

  def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) {
    val negotiatedResponse = negotiateResponseMediaType match {
      case Some(mediaType) => responseForMediaType(mediaType)
      case _ => StatusResponse(SC_NOT_ACCEPTABLE)
    }
    negotiatedResponse.send(httpRequest, httpResponse)
  }

  private def negotiateResponseMediaType(): Option[MediaType] =
    HttpHelpers.acceptedContentTypes(RequestContext.request.getHeaders("Accept"), haveMediaTypes).headOption

  def haveMediaTypes: Seq[MediaType] = Seq.empty

  def responseForMediaType(mediaType: MediaType): Response = StatusResponse(SC_NOT_ACCEPTABLE)
}


