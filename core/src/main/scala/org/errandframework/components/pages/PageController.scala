/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package pages

import javax.servlet.http.HttpServletResponse._
import org.errandframework.http._
import org.errandframework.http.Methods._

/**
 * Instantiate a page and serve it.
 * It's important that "page" not be evaluated before the request arrives because we want to be able
 * to create instances of this controller and assign them to URLs without creating the page until it's needed.
 * For example, we may create a page with lots of links with dynamic URLs.  We need a controller to assign to
 * each URL.  But we don't actually want to create all the pages.  We only want to create the page when
 * the client actually follows one of the links.
 * PageController implements ParameterProcessor and retrieves parameter information from the Page.
 */
class PageController(newPage: => Page) extends Controller with ParameterProcessor {

  /**
   * Lazily-evaluated val to insure that the page is instantiated (assuming that "page" evaluates to
   * something like "new SomePage") only once.
   */
  private lazy val page: Page = newPage

  override def respond(request: Request) = {

    // PageController only handles GET requests.  POST requests should be going to other controllers.
    // Of course those other controllers might return PageResponse.

    request.method match {
      case GET => Page.withValue(Some(page))(super.respond(request))
      case _ => StatusResponse(SC_METHOD_NOT_ALLOWED)
    }
  }

  def parameters = page.parameters

  override def validate() = page.validate

  def onValid(request: Request) = if (!page.visible)
    throw new InvisiblePageException(page)
  else if (!page.enabled)
    throw new DisabledPageException(page)
  else
    PageResponse(page)
}