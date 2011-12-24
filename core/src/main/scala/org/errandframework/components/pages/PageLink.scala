/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package pages

/**
 * PageLink is a link to a page.
 * The page does not already exist; it is created on demand with the newPage method.
 */
abstract class PageLink(content: Component) extends Link(content) with EmbeddedController {

  def controller = new PageController(newPage)

  def newPage: Page
}

object PageLink {

  def apply(content: Component, linkNewPage: => Page, linkBehaviors: Behavior*) = new PageLink(content) {
    override def behaviors = linkBehaviors
    def newPage = linkNewPage
  }
}
