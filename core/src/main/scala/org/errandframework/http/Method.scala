/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

// TODO, case class.

/**
 * Wrap the HTTP method in a class in order to distinguish it from a URL (which is always a String).
 */
class Method(val name: String) {

//  def unapply(method: Method) = method.name == name
//
//  def unapply(request: Request) = if (request.method == this) Some((request.scheme, request.host, request.path)) else None

  override def equals(that: Any) = that match {
    case method: Method => name == method.name
    case _ => false
  }

  final override def hashCode() = name.hashCode

  final override def toString() = name
}

object Method {
  def apply(name: String) = new Method(name)
//  def unapply(method: Method) = Some(method.name)
}
