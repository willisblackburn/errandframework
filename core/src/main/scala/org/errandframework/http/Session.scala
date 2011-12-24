/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import javax.servlet.http.HttpSession
import scala.collection.JavaConversions.enumerationAsScalaIterator

/**
 * Provides access to the servlet session associated with the current request.
 */
class Session(val httpSession: HttpSession) extends Scope {

  def getAttribute(name: String) = Option(httpSession.getAttribute(name))

  def setAttribute(name: String, value: Any) {
    httpSession.setAttribute(name, value)
  }

  def removeAttribute(name: String) {
    httpSession.removeAttribute(name)
  }

  def attributeNames = httpSession.getAttributeNames match {
    case null => Seq.empty
    case names => names.toSeq
  }
}
