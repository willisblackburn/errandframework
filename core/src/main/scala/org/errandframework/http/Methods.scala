/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * Instances of Method for common HTTP methods.
 */
object Methods {
  val OPTIONS = Method("OPTIONS")
  val GET = Method("GET")
  val HEAD = Method("HEAD")
  val POST = Method("POST")
  val PUT = Method("PUT")
  val DELETE = Method("DELETE")
  val TRACE = Method("TRACE")
  val CONNECT = Method("CONNECT")
}
