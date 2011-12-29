package org.errandframework.examples.features

import org.errandframework.components.pages.Page
import org.errandframework.http._

import FeaturesExampleApplication._
import org.errandframework.components.{MessagePanel, Link, Component}

/**
 * FeaturePage is the parent class of all pages but HomePage and demonstrates simple page inheritance.
 * FeaturePage defines bodyContent and offers another property, featureContent, for subclasses to define.
 * It also sets the page title.
 */
abstract class FeaturePage extends Page {

  final def bodyContent = {
    <div>
      <h1>{title}</h1>
      {messagePanel.render}
      {featureContent.render}
      <p>{homeLink.render}</p>
    </div>
  }

  def featureContent: Component

  /**
   * Create a MessagePanel for every page.
   */
  private val messagePanel = new MessagePanel

  private val homeLink = Link("Home", rootLocation.toUrl())
}
