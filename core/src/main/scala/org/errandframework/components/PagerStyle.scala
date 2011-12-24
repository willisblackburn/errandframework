/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import util.DynamicVariable
import java.text.NumberFormat
import xml.{Elem, Text, NodeSeq}

/**
 * Methods to render pagers.
 */
trait PagerStyle {

  def renderPager(component: Pager): NodeSeq
}

object PagerStyle extends DynamicVariable[PagerStyle](DefaultPagerStyle) with PagerStyle {

  def renderPager(component: Pager) = value.renderPager(component)
}

object DefaultPagerStyle extends PagerStyle {
  def renderPager(component: Pager) = component.renderIfVisible {
    val format = NumberFormat.getIntegerInstance
    def numberedLink(page: Int) = pageLink(page)(format.format(page), if (page == component.page) "errand-Pager-current" else "errand-Pager-pager")
    def pageLink(page: Int)(text: String, cssClass: String) = Link(text, component.linkUrl(page), Behavior.cssClass(cssClass)).render
    def spacer(text: String, cssClass: String) = { <span class={cssClass}>{text}</span> }
    <div class="errand-Pager">
      {component.previousPage.map(pageLink).getOrElse(spacer _)("Previous", "errand-Pager-previous")}
      {component.pages.flatMap(numberedLink)}
      {component.nextPage.map(pageLink).getOrElse(spacer _)("Next", "errand-Pager-next")}
      <span class="errand-Pager-display">Displaying {format.format(component.firstDisplayed)}-{format.format(component.lastDisplayed)} of {format.format(component.count)}</span>
    </div>
  }
}
