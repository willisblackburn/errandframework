/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

// TODO, maybe just use mutable.Map for this interface?  Probably have an "attributes" map for Request, Session, etc.

/**
 * Scope abstracts get/set attribute behavior of HttpServletRequest, HttpSession, and ServletContext.
 * The names from the servlet scopes request, session, and application.
 */
trait Scope {

  def getAttribute(name: String): Option[Any]

  def setAttribute(name: String, value: Any)

  def removeAttribute(name: String)

  def attributeNames: Seq[String]
}

