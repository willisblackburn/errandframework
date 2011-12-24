/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package ajax

import org.errandframework.components.{Component, Behavior}
import xml.{Attribute, Null, Text, Elem}

/**
 * Alters a SubmitButton or ActionButton so it submits the form via AJAX instead of through the regular form submit process.
 */
object AjaxButtonBehavior extends Behavior {

  def transform(component: Component, elem: Elem) = {
    AjaxSupport.add
    // There are two possibilities:
    // SubmitButton is a regular form submit button.  AJAX-ize it by retrieving the form's action URL and submitting
    // the form to that URL via AJAX.
    // ActionButton is submit button that only works with ActionForms.  The form's action URL is the dynamic controller's
    // URL, and the button provides the value for the controller ID parameter.  AJAX-ize the form by submitting
    // to the form's action URL (as above) and also append the controller ID parameter name and value.
    // We can generalize this as follows:  Always submit to the action URL, and append the button name and value to
    // the form data, if it is present.
    elem % Attribute("onclick", Text("return ajaxButton(this)"), Null)
  }
}
