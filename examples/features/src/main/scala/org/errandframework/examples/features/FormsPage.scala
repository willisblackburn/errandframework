package org.errandframework.examples.features

import org.errandframework.components.pages.{Page, PageResponse}
import org.errandframework.components._

import FeaturesExampleApplication._

/**
 * Demonstrates form-generation and handling features.
 */
class FormsPage extends FeaturePage {

  def featureContent = {
    <div>
      <h2>Simple Form Component</h2>
      {SimpleFormPage.description}
      {simpleFormLink.render}
      <h2>Action Forms</h2>
      <p>In most cases you'll want ActionForm.  It supports the general use case of displaying a form, letting the
        user submit it, validating the fields, and finally either taking some action after a
        successful validation or redisplaying the form with appropriate error messages.</p>
      {ActionFormPage.description}
      {actionFormLink.render}
      {ComplexActionFormPage.description}
      {complexActionFormLink.render}
    </div>
  }

  private val simpleFormLink = Link("Simple Form Example", simpleFormLocation.toUrl())
  private val actionFormLink = Link("ActionForm Example", actionFormLocation.toUrl())
  private val complexActionFormLink = Link("Complex ActionForm Example", complexActionFormLocation.toUrl())

  override def title = "Forms"
}

