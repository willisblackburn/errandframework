/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import util.DynamicVariable
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

/**
 * Interface for a thread-bound object that stores information about the ongoing request.
 * Available from any class during request processing via the RequestContext companion object.
 */
class RequestContext(val httpRequest: HttpServletRequest, val httpResponse: HttpServletResponse, val servlet: ErrandServlet) {

  def httpSession = httpRequest.getSession

  def servletContext = httpRequest.getServletContext

  lazy val request = new Request(httpRequest, httpResponse)

  lazy val session = new Session(httpSession)

  lazy val application = new Application(servletContext)

  def hasSession = httpRequest.getSession(false) != null

  /**
   * Wrap the encodeURL method of HttpServletResponse.
   */
  def encodeURL(url: String) = httpResponse.encodeURL(url)

  /**
   * Similar to encodeUrl but wraps encodeRedirectURL instead of encodeURL.
   * @param url the URL.
   * @return the encoded URL.
   */
  def encodeRedirectURL(url: String) = httpResponse.encodeRedirectURL(url)
}

object RequestContext extends DynamicVariable[Option[RequestContext]](None) {

  def httpRequest = get.httpRequest

  def httpResponse = get.httpResponse

  def httpSession = get.httpSession

  def servlet = get.servlet

  def request = get.request

  def session = get.session

  def application = get.application

  def hasSession = get.hasSession

  def encodeURL(url: String) = get.encodeURL(url)

  def encodeRedirectURL(url: String) = get.encodeRedirectURL(url)

  def get() = value.getOrElse(throw new RuntimeException("No RequestContext bound to the current thread"))
}
