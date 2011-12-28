package org.errandframework.examples.features

import org.errandframework.components.{Link, Component}
import org.errandframework.components.pages.Page
import org.errandframework.http._

import FeaturesExampleApplication._

/**
 * FeaturePage is the parent class of all pages but HomePage and demonstrates simple page inheritance.
 * FeaturePage defines bodyContent and offers another property, featureContent, for subclasses to define.
 * It also sets the page title.
 */
abstract class FeaturePage extends Page {

  final def bodyContent = {
    <h1>{title}</h1> ++
    featureContent.render ++
    <p>{homeLink.render}</p>
  }

  def featureContent: Component

  private val homeLink = Link("Home", rootLocation.toUrl())
}
