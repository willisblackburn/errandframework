/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package ajax

import org.errandframework.http.Path
import org.errandframework.components.{Script, MooToolsSupport}
import org.errandframework.http.{ResourceServerControllerProvider, ErrandServlet}
import pages.Page
import ResourceServerControllerProvider.urlForResource

/**
 * Component that adds AJAX support using MooTools to the page.
 */
object AjaxSupport {

  def add() {
    MooToolsSupport.add
    Page.addHeadContent(script)
  }

  private val script = ExternalScript(urlForResource(Path(getClass.getPackage) / "AjaxSupport.js"))
}