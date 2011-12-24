/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package forms

import util.DynamicVariable
import xml.NodeSeq
import pages.Page

/**
 * Strategy for rendering a BubbleField component.
 */
trait BubbleFieldStyle {

  def renderBubbleField(component: BubbleField[_]): NodeSeq
}

object BubbleFieldStyle extends DynamicVariable[BubbleFieldStyle](DefaultBubbleFieldStyle) with BubbleFieldStyle {

  def renderBubbleField(component: BubbleField[_]) = value.renderBubbleField(component)
}

object DefaultBubbleFieldStyle extends BubbleFieldStyle {

  import FormStyle.disabled

  def renderBubbleField(component: BubbleField[_]) = component.renderIfVisible {
    Page.addHeadContent(styleSheet)
    component.transform(<span class="errand-BubbleField">
      <ul id={component.bubbleListId} class="bubbleList">{for (value <- component.encode) yield <li>{value}</li>}</ul>
      <input type="text" disabled={disabled(component)} onkeydown={component.onKeyDownJavaScript}/>
    </span>)
  }

  val styleSheet = StyleSheet("""
span.errand-BubbleField ul.bubbleList {
    list-style: none;
    margin: 0;
    padding: 0;
    display: inline;
}
span.errand-BubbleField ul.bubbleList li {
    display: inline;
    padding: 3px;
    background-color: lightBlue;
    margin: 0 2px 0 0;
}
span.errand-BubbleField ul.bubbleList li a {
    margin-left: 4px;
}
  """)
}