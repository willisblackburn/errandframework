/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components.pages

import xml.NodeSeq
import util.DynamicVariable

/**
 * PageStyle defines renderers for Page instances.
 */
trait PageStyle {

  def renderPage(page: Page): NodeSeq
}

object PageStyle extends DynamicVariable[PageStyle](DefaultPageStyle) with PageStyle {

  def renderPage(page: Page) = value.renderPage(page)
}

object DefaultPageStyle extends PageStyle {

  def renderPage(page: Page) = {
    // Invoke headContent first, then bodyContent.  We want to actually resolve the headContent first,
    // but give the bodyContent a chance to add additional children to the head before rendering it.
    val headXhtml = page.headContent.render
    val bodyXhtml = page.bodyContent.render
    val additionalHeadXhtml = NodeSeq.fromSeq(page.additionalHeadContents.flatMap(_.render))
    <html xmlns="http://www.w3.org/1999/xhtml" xmlns:errand="http://www.errandframework.org/schemas/errand0.1.xsd">
      <head>
        <title>{page.title}</title>
        {headXhtml}
        {additionalHeadXhtml}
      </head>
      {page.transform(<body class="errand-Body">
        {bodyXhtml}
      </body>)}
    </html>
  }
}
