/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

/**
 * Creates an inline style sheet.
 * Must be rendered into the head by including it in the headContent for a page or by calling Page.addHeadContent.
 */
class StyleSheet(css: String) extends Component {

  def render() = <style type="text/css">{css}</style>
}

object StyleSheet {

  def apply(css: String) = new StyleSheet(css)
}

class ExternalStyleSheet(url: String) extends Component {

  def render() = <link rel="stylesheet" href={url}/>
}

object ExternalStyleSheet {

  def apply(url: String) = new ExternalStyleSheet(url)
}

