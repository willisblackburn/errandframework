/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package ajax

import util.DynamicVariable
import xml.NodeSeq
import forms.FormStyle
import pages.Page

/**
 * Renders AjaxTextField instances.
 */
trait AjaxTextFieldStyle {

  def renderAjaxTextField[T](component: AjaxTextField[T]): NodeSeq
}

object AjaxTextFieldStyle extends DynamicVariable[AjaxTextFieldStyle](DefaultAjaxTextFieldStyle) with AjaxTextFieldStyle {

  def renderAjaxTextField[T](component: AjaxTextField[T]) = value.renderAjaxTextField(component)
}

object DefaultAjaxTextFieldStyle extends AjaxTextFieldStyle {

  import FormStyle.disabled

  def renderAjaxTextField[T](component: AjaxTextField[T]) = component.renderIfVisible {
    Page.addHeadContent(styleSheet)
    component.transform(<span class="errand-AjaxTextField">
      <ul id={component.menuId} class="menu"></ul>
      <input type="text" name={component.name} disabled={disabled(component)} onkeydown={component.onKeyDownJavaScript} onblur={component.onBlurJavaScript}/>
    </span>)
  }

  val styleSheet = StyleSheet("""
span.errand-AjaxTextField {
    position: relative;
}
span.errand-AjaxTextField ul.menu {
    display: none;
    position: absolute;
    top: 18px;
    left: 2px;
    width: auto;
    height: auto;
    margin: 0;
    padding: 0;
    background-color: white;
    border: 1px silver solid;
    list-style: none;
    font: 10pt Arial, sans-serif;
}
span.errand-AjaxTextField ul.menu li {
    margin: 2px;
}
span.errand-AjaxTextField ul.menu li.selected {
    background-color: silver;
}
  """)
}
