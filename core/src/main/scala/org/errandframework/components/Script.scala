/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

import org.errandframework.util.XmlHelpers._

/**
 * Component that renders a script tag for JavaScript.
 */
class Script extends Component {

  def render() = <script src={asText(url)}>{asUnparsed(javaScript.map(wrap).getOrElse(""))}</script>

  // TODO, do I need wrap?

//  private def wrap(s: String) = "//<![CDATA[\n" + s + "\n//]]>"
  private def wrap(s: String) = s

  def url: Option[String] = None

  def javaScript: Option[String] = None
}

object Script {

  def apply(scriptJavaScript: String) = new Script {
    override def javaScript = Some(scriptJavaScript)
  }

  def fromUrl(scriptUrl: String) = new Script {
    override def url = Some(scriptUrl)
  }
}
