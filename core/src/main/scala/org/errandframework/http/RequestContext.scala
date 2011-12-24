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
   * Wrap the encodeURL method of HttpServletResponse and also invokes expandPaths, which translates an initial
   * "/./" or "/././" to the context path or the context plus servlet path, respectively.
   * @param baseUrl the URL.
   * @return the encoded URL.
   */
  def encodeURL(url: String) = httpResponse.encodeURL(url)

  /**
   * Similar to encodeUrl but wraps encodeRedirectURL instead of encodeURL.
   * @param baseUrl the URL.
   * @return the encoded URL.
   */
  def encodeRedirectURL(url: String) = httpResponse.encodeRedirectURL(url)

//  // TODO, generalize this into a way to identify the part of the URL tree that we're in so that
//  // the application can invoke, say, getBasePath, to get the part of the path that should be assumed.
//  // It would default to the context/servlet path, but there would be a way to add to it.
//  // Maybe this is a stupid idea.
//
//  val CONTEXT_DOT_PATH = "/./"
//  val CONTEXT_DOT_PATH_LENGTH_MINUS_1 = CONTEXT_DOT_PATH.length - 1
//  val CONTEXT_SERVLET_DOT_PATH = "/././"
//  val CONTEXT_SERVLET_DOT_PATH_LENGTH_MINUS_1 = CONTEXT_SERVLET_DOT_PATH.length - 1
//
//  /**
//   * Replaces "/././" at the beginning of the URL with the servlet path or "/./" with the context path,
//   * so that (for example) context-relative URLs may be written as "/./path/to/the/resource" without requiring a
//   * special tag.
//   */
//  def expandPaths(url: String) = if (url.startsWith(CONTEXT_SERVLET_DOT_PATH))
//    request.contextServletPath + url.substring(CONTEXT_SERVLET_DOT_PATH_LENGTH_MINUS_1)
//  else if (url.startsWith(CONTEXT_DOT_PATH))
//    request.contextPath + url.substring(CONTEXT_DOT_PATH_LENGTH_MINUS_1)
//  else
//    url
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

  /**
   * Similar to encodeUrl but wraps encodeRedirectURL instead of encodeURL.
   * @param baseUrl the URL.
   * @return the encoded URL.
   */
  def encodeRedirectURL(url: String) = get.encodeRedirectURL(url)

  def get() = value.getOrElse(throw new RuntimeException("No RequestContext bound to the current thread"))
}
