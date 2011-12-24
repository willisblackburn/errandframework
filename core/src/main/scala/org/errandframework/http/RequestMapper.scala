/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * RequestMapper translates between a request URL and a Controller.
 */
trait RequestMapper {

  def resolve(request: Request): Seq[(Controller, Int)]
}
