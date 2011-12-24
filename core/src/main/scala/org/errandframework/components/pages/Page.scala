/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package pages

import collection.mutable
import mutable.LinkedHashSet
import xml.NodeSeq
import org.errandframework.components.Component
import util.DynamicVariable
import org.errandframework.http._

/**
 * Represents an XHTML page.
 * The only requirement of a Page it that it be able to render itself as XHTML.
 */
abstract class Page extends Component {

  def render(): NodeSeq = PageStyle.renderPage(this)

  def parameters: Seq[RequestParameter[_]] = Seq.empty

  def validate(): Boolean = true

  def headContent: Component = Component.empty

  /**
   * A collection in which to accumulate new tags for the head section.
   * The additional head content is maintained in the request and so is regenerated with each render.
   * We use a LinkedHashSet to preserve the order in which the head content was added (to avoid weird bugs that might
   * only occur if the head content was rendered in some unexpected order) but also for de-duping purposes.
   * (Note that Component implementations must provide equals and hashCode implementations for this
   * to work well and consistently.)
   */
  private val _additionalHeadContents = RequestVar(new LinkedHashSet[Component])

  def addHeadContent(content: Component) {
    _additionalHeadContents.value += content
  }

  /**
   * Accessor so the page style class can retrieve the additional head content.
   */
  def additionalHeadContents: Seq[Component] = _additionalHeadContents.value.toSeq

  def bodyContent: Component

  def title: String = "Untitled"

  def resourceBundleNames: List[String] = ClassPathHelpers.expandSuperclasses(getClass).map(_.getName)
}

object Page extends DynamicVariable[Option[Page]](None) {

  def addHeadContent(content: Component) {
    get.addHeadContent(content)
  }

//  def registerController(component: Component, newController: Controller) = get.registerController(component, newController)

  def get() = value.getOrElse(throw new RuntimeException("No Page bound to the current thread"))
}


