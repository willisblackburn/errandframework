/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import java.util.UUID

/**
 * ScopeVar is a variable that is stored in one of the three servlet scopes:  request, session, and application.
 * It offers a type-safe alternative to the generic getAttribute and setAttribute methods.
 */
abstract class ScopeVar[T, S <: Scope](init: => T) {

  val name = UUID.randomUUID.toString

  final def value = valueOption getOrElse {
    val initValue = init
    scope.setAttribute(name, initValue)
    initValue
  }

  final def valueOption = scope.getAttribute(name).map(_.asInstanceOf[T])

  final def value_=(value: T) {
    scope.setAttribute(name, value)
  }

  final def clear() {
    scope.removeAttribute(name)
  }

  def scope: S
}

class RequestVar[T](init: => T) extends ScopeVar[T, Request](init) {
  def scope = RequestContext.request
}

object RequestVar {
  def apply[T](init: => T = throw new RuntimeException("Uninitialized RequestVar")) = new RequestVar[T](init)
}

class SessionVar[T](init: => T) extends ScopeVar[T, Session](init) {
  def scope = RequestContext.session
}

object SessionVar {
  def apply[T](init: => T = throw new RuntimeException("Uninitialized SessionVar")) = new SessionVar[T](init)
}

class ApplicationVar[T](init: => T) extends ScopeVar[T, Application](init) {
  def scope = RequestContext.application
}

object ApplicationVar {
  def apply[T](init: => T = throw new RuntimeException("Uninitialized ApplicationVar")) = new ApplicationVar[T](init)
}
