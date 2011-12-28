/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * A trait to be implemented by classes that need to filter requests and/or responses.
 */
trait RequestFilter {

  /**
   * Performs the filtering function.
   * The filter may either generate its own response (if it has intercepted the request and does not want any
   * further processing to occur) or invoke the respond function in order to continue processing.
   * @param request the request.
   * @param respond a function that produces the response. It will invoke either the next filter in the filter
   * chain, or the controller's respond method.
   */
  def filter(request: Request)(respond: Request => Response): Response
}
