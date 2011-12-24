/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package ajax

import xml.NodeSeq
import util.DynamicVariable
import pages.Page
import forms.{DefaultBubbleFieldStyle, FormStyle}

/**
 * Renders AjaxBubbleField instances.
 */
trait AjaxBubbleFieldStyle {

  def renderAjaxBubbleField[T](component: AjaxBubbleField[T]): NodeSeq
}

object AjaxBubbleFieldStyle extends DynamicVariable[AjaxBubbleFieldStyle](DefaultAjaxBubbleFieldStyle) with AjaxBubbleFieldStyle {

  def renderAjaxBubbleField[T](component: AjaxBubbleField[T]) = value.renderAjaxBubbleField(component)
}

object DefaultAjaxBubbleFieldStyle extends AjaxBubbleFieldStyle {

  import FormStyle.disabled

  def renderAjaxBubbleField[T](component: AjaxBubbleField[T]) = component.renderIfVisible {
    Page.addHeadContent(DefaultBubbleFieldStyle.styleSheet)
    Page.addHeadContent(DefaultAjaxTextFieldStyle.styleSheet)
    component.transform(<span class="errand-BubbleField">
      <ul id={component.bubbleListId} class="bubbleList">{for (value <- component.encode) yield <li>{value}</li>}</ul>
      <span class="errand-AjaxTextField">
        <ul id={component.menuId} class="menu"></ul>
        <input type="text" disabled={disabled(component)} onkeydown={component.onKeyDownJavaScript} onblur={component.onBlurJavaScript}/>
      </span>
    </span>)
  }
}



