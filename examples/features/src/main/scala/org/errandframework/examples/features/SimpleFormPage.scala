package org.errandframework.examples.features

import org.errandframework.http._
import org.errandframework.components.{Component, Link}
import org.errandframework.components.forms.Form

/**
 * Demonstrates very simple forms.
 */
class SimpleFormPage extends FeaturePage {

  def featureContent = {
    <div>
      {SimpleFormPage.description}
      {form.render}
      <p>{formsLink.render}</p>
    </div>
  }

  private val form = new Form {

    def content = {
      <div>Query <input name="q" type="text"/></div>
      <div>Start <select name="start"><option>0</option><option>10</option><option>20</option><option>30</option><option>40</option></select></div>
      <div><button type="submit">Search</button></div>
    }

    def url = "http://www.google.com/search"

    override def method = Methods.GET
  }

  private val formsLink = Link("Forms", rootLocation.toUrl())

  override def title = "Simple Form"
}

object SimpleFormPage {

  // We can reuse XHTML snippets just like we'd reuse any other type of code in Scala.

  val description = <p>You can use plain old HTML to create forms. The action URL can be either external or internal.
    You can also build forms as components.  The base Form class will render the form but does not offer any
    other features.</p>
}
