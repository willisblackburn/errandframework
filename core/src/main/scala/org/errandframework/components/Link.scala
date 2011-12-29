/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import org.errandframework.http._

// TODO, PageLink that accepts Assignment objects.
// TODO, some kind of link that also accepts Assignment objects.  Actually this will go away if I have a Url class that
// can include parameters.

/**
 * A component that renders a link to a URL.
 * The URL can be either an internal path or an external URL.
 * Internal paths are usually generated using the Location class but may be specified directly as well.
 * The link may include embedded parameters, and the implementation may also override the parameters property
 * to include additional name/value pairs.
 * Link is pretty low-level, so it doesn't know about Parameter or ParameterValue.
 */
abstract class Link(val content: Component) extends Component {

  def render() = LinkStyle.renderLink(this)

  def url: String
}

/**
 * Utility methods for generating HTML links.
 */
object Link {

  def apply(linkUrl: String, linkBehaviors: Behavior*): Link = apply(linkUrl, linkUrl, linkBehaviors: _*)

  def apply(content: Component, linkUrl: String, linkBehaviors: Behavior*) = new Link(content) {
    val url = linkUrl
    override def behaviors = linkBehaviors
  }
}

abstract class ActionLink(content: Component) extends Link(content) with EmbeddedController {

  def controller = new Controller {

    def respond(request: Request) = onClick
  }

  def onClick(): Response
}

object ActionLink {

  def apply(content: Component, linkBehaviors: Behavior*)(linkOnClick: => Response) = new ActionLink(content) {
    override def behaviors = linkBehaviors
    def onClick() = linkOnClick
  }
}

