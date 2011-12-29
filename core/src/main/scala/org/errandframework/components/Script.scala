/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import org.errandframework.util.XmlHelpers._

/**
 * Component that renders a script tag for JavaScript.
 */
class Script(val javaScript: String) extends Component {

  def render() = <script>{asUnparsed(javaScript)}</script>

  override def equals(that: Any) = that match {
    case script: Script => javaScript == script.javaScript
    case _ => false
  }

  override def hashCode() = javaScript.hashCode
}

object Script {

  def apply(javaScript: String) = new Script(javaScript)
}

class ExternalScript(val url: String) extends Component {

  def render() = <script src={asText(url)}/>

  override def equals(that: Any) = that match {
    case script: ExternalScript => url == script.url
    case _ => false
  }

  override def hashCode() = url.hashCode
}

object ExternalScript {

  def apply(url: String) = new ExternalScript(url)
}

