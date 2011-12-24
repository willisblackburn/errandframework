/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import xml.NodeSeq
import util.DynamicVariable
import org.errandframework.http.RequestContext

/**
 * Methods to render links.
 */
trait LinkStyle {

  def renderLink(component: Link): NodeSeq
}

object LinkStyle extends DynamicVariable[LinkStyle](DefaultLinkStyle) with LinkStyle {

  def renderLink(component: Link) = value.renderLink(component)
}

object DefaultLinkStyle extends LinkStyle {

  def renderLink(component: Link) = component.renderIfVisible {
    component.enabled match {
      case true =>
        component.transform(<a href={RequestContext.encodeURL(component.url)} class="errand-Link">{component.content.render}</a>)
      case _ =>
        component.content.render
    }
  }
}
