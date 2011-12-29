/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import org.errandframework.http.Path
import org.errandframework.http.ResourceServerControllerProvider
import pages.Page
import ResourceServerControllerProvider.urlForResource

/**
 * Component that adds the MooTools library to the page.
 */
object MooToolsSupport {

  def add() {
    Page.addHeadContent(script)
  }

  private val script = ExternalScript(urlForResource(Path(getClass.getPackage) / "mootools-core-1.3.2-full-nocompat.js"))
}