/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

/**
 * Creates an inline style sheet.
 * Must be rendered into the head by including it in the headContent for a page or by calling Page.addHeadContent.
 */
class StyleSheet(val css: String) extends Component {

  def render() = <style type="text/css">{css}</style>

  override def equals(that: Any) = that match {
    case styleSheet: StyleSheet => css == styleSheet.css
    case _ => false
  }

  override def hashCode() = css.hashCode
}

object StyleSheet {

  def apply(css: String) = new StyleSheet(css)
}

class ExternalStyleSheet(val url: String) extends Component {

  def render() = <link rel="stylesheet" href={url}/>

  override def equals(that: Any) = that match {
    case styleSheet: ExternalStyleSheet => url == styleSheet.url
    case _ => false
  }

  override def hashCode() = url.hashCode
}

object ExternalStyleSheet {

  def apply(url: String) = new ExternalStyleSheet(url)
}

