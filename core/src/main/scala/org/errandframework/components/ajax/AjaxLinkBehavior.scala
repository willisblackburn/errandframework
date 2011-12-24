/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components.ajax

import org.errandframework.components.{Component, Behavior}
import xml.Attribute._
import xml.{Attribute, Null, Text, Elem}

/**
 * Changes the link URL from a regular URL to one that is invoked via ajaxRequest.
 */
object AjaxLinkBehavior extends Behavior {

  def transform(component: Component, elem: Elem) = {
    AjaxSupport.add
    elem.attribute("href") match {
      case Some(url) => elem % Attribute("href", Text("javascript:ajaxLink('" + url + "')"), Null)
      case _ => elem
    }
  }
}
