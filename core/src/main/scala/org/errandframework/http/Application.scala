/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import javax.servlet.ServletContext
import java.net.URL
import java.io.InputStream
import scala.collection.JavaConversions.enumerationAsScalaIterator

/**
 * Object that represents the application.
 * Application depends only on the servlet context and so is effectively shared across however many ErrandServlet
 * instances are present in the web application, although each request will gets its own instance.
 */
class Application(servletContext: ServletContext) extends Scope {

  /**
   * Loads a resource from the servlet context and returns it as an Option[URL].
   * @param path the path to the resource, as a list.
   * @return Some(URL) or None if the resource was not found.
   */
  def getResource(path: Path): Option[URL] = Option(servletContext.getResource(path.toString))

  /**
   * Loads a resource from the servlet context and returns it as an Option[InputStream].
   * @param path the path to the resource, as a list.
   * @return Some(InputStream) or None if the resource was not found.
   */
  def getResourceAsStream(path: Path): Option[InputStream] = Option(servletContext.getResourceAsStream(path.toString))

  def getAttribute(name: String) = Option(servletContext.getAttribute(name))

  def setAttribute(name: String, value: Any) {
    servletContext.setAttribute(name, value)
  }

  def removeAttribute(name: String) {
    servletContext.removeAttribute(name)
  }

  def attributeNames = servletContext.getAttributeNames match {
    case null => Seq.empty
    case names => names.toSeq
  }
}
