/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * A trait to be implemented by classes that need to filter requests and/or responses.
 */
trait RequestFilter {

  def filter(request: Request)(respond: Request => Response): Response
}
