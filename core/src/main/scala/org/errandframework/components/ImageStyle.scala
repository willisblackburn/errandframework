/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

import xml.NodeSeq
import util.DynamicVariable
import org.errandframework.http.RequestContext
import org.errandframework.util.XmlHelpers._

/**
 * Methods to render links.
 */
trait ImageStyle {

  def renderImage(component: Image): NodeSeq
}

object ImageStyle extends DynamicVariable[ImageStyle](DefaultImageStyle) with ImageStyle {

  def renderImage(component: Image) = value.renderImage(component)
}

object DefaultImageStyle extends ImageStyle {

  def renderImage(component: Image) = component.renderIfVisible {
    component.transform(<img src={RequestContext.encodeURL(component.url)} alt={asText(component.alt)} title={asText(component.title)} width={asText(component.width)} height={asText(component.height)}/>)
  }
}
