/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package forms

import org.errandframework.http._
import org.errandframework.http.Methods._
import xml.NodeSeq

/**
 * Base class for forms.  Supports specifying the method, encoding type, URL, and the form fields.
 */
abstract class Form extends Component {

  def render() = FormStyle.renderForm(this)

  /**
   * The method to use for the form.
   */
  def method: Method = POST

  /**
   * The encoding type to use for the form.
   */
  def encodingType: MediaType = MediaType("application", "x-www-form-urlencoded")

  def content: Component

  def url: String
}

abstract class ActionForm extends Form {

  def url = DynamicControllerProvider.get.dynamicLocation.toUrl()

  protected def renderEditors(editors: Seq[Editor[_]]): NodeSeq = editors flatMap {
    case field: LabeledField[_] => field.render
    case field: Field[_] => <div>{field.name} {field.render}</div>
    case editor => editor.render
  }
}
