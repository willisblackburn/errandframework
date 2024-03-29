/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package ajax

import org.errandframework.components.Component
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import xml.NodeSeq
import pages.Page
import org.errandframework.util.Log
import org.errandframework.http.{MediaType, CharacterBufferResponse, Response}

/**
 * AjaxResponse sends updates to a set of components to the browser.
 * Each of the components must have an element ID.
 */
class AjaxResponse(page: Page, components: Component*) extends Response with Log {

  def send(httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) = {
    def renderComponent(component: Component): NodeSeq = component.id match {
      case Some(id) => component.render
      case _ =>
        log.warn("Component used in AjaxResponse does not have an ID: " + component)
        Seq.empty
    }
    val xhtml = Page.withValue(Some(page))(NodeSeq.fromSeq(components.flatMap(renderComponent)))
    CharacterBufferResponse(MediaType("application", "xml"), xhtml.toString).send(httpRequest, httpResponse)
  }
}

object AjaxResponse {

  def apply(page: Page, components: Component*) = new AjaxResponse(page, components: _*)
}