/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import scala.collection.JavaConversions.{enumerationAsScalaIterator, collectionAsScalaIterable}

/**
 * Scala-friendly wrapper for the servlet request.
 */
class Request(val httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) extends Scope {

  def method = Method(httpRequest.getMethod)

  def scheme = httpRequest.getScheme

  def host = httpRequest.getServerName

  def port = httpRequest.getServerPort

  def portOption: Option[Int] = (scheme, port) match {
    case ("http", 80) => None
    case ("https", 443) => None
    case (_, port) => Some(port)
  }

  def path = httpRequest.getPathInfo match {
    case null => Path.empty
    case path => Path(path)
  }

  def getAttribute(name: String) = Option(httpRequest.getAttribute(name))

  def setAttribute(name: String, value: Any) {
    httpRequest.setAttribute(name, value)
  }

  def removeAttribute(name: String) {
    httpRequest.removeAttribute(name)
  }

  def attributeNames = httpRequest.getAttributeNames match {
    case null => Seq.empty
    case names => names.toSeq
  }

  def getParameter(name: String) = Option(httpRequest.getParameter(name))

  def getParameters(name: String) = httpRequest.getParameterValues(name) match {
    case null => Seq.empty
    case values => values.toSeq
  }

  def parameterNames = httpRequest.getParameterNames match {
    case null => Seq.empty
    case names => names.toSeq
  }

  def parameterMap: Map[String, Seq[String]] = Map(parameterNames.map(name => (name, getParameters(name))): _*)

  def getHeader(name: String) = Option(httpRequest.getHeader(name))

  def getHeaders(name: String) = httpRequest.getHeaders(name) match {
    case null => Seq.empty
    case headers => headers.toSeq
  }

  def headerNames = httpRequest.getHeaderNames match {
    case null => Seq.empty
    case names => names.toSeq
  }

  def headerMap: Map[String, Seq[String]] = Map(headerNames.map(name => (name, getHeaders(name))): _*)

  def getCookie(name: String) = httpRequest.getCookies.find(_.getName == name)

  def cookies = httpRequest.getCookies match {
    case null => Seq.empty
    case cookies => cookies.toSeq
  }

  def getPart(name: String) = Option(httpRequest.getPart(name))

  def parts = httpRequest.getParts match {
    case null => Seq.empty
    case parts => parts.toSeq
  }

  /**
   * Context path starts with a '/' but does not end with one.
   * The root context is an empty string without any slashes.
   */
  def contextPath = Path(httpRequest.getContextPath)

  /**
   * Servlet path starts with a '/' but does not end with one.
   * If the servlet is mapped to all paths then the servlet path is an empty string without any slashes.
   */
  def servletPath = Path(httpRequest.getServletPath)

  def contextServletPath = Path(httpRequest.getContextPath + httpRequest.getServletPath)

  override def toString() = httpRequest.getMethod + " " + httpRequest.getRequestURI
}

