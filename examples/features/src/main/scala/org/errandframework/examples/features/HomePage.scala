package org.errandframework.examples.features

import org.errandframework.components.pages.Page
import org.errandframework.components.Link

import FeaturesExampleApplication._

/**
 * The main page of the application.
 * There's nothing special about this page.  It's just the page that's mapped to rootLocation.
 */
object HomePage extends Page {

  /**
   * Pages only have one required member, bodyContent, which defines all of the HTML tags within the &lt;body&gt;
   * tag.
   * Override headContent or call Page.addHeadContent to modify the contents of the &lt;head&gt; tag.
   * The type of both bodyContent and headContent is Component, but since there is an implicit conversion from
   * NodeSeq to Component, we can just put XHTML here.
   */
  def bodyContent = {
    <h1>Welcome to Errand Web Framework</h1>
    <h2>Features</h2>
    <ul>
      <li>{linksLink.render}</li>
      <li>{urlsLink.render}</li>
      <li>{formsLink.render}</li>
      <li>{repeatersLink.render}</li>
      <li>{scopeVariablesLink.render}</li>
    </ul>
  }

  private val linksLink = Link("Links", linksLocation.toUrl())
  private val urlsLink = Link("URLs, Paths, and Parameters", urlsLocation.toUrl())
  private val formsLink = Link("Forms", formsLocation.toUrl())
  private val repeatersLink = Link("Repeaters", repeatersLocation.toUrl())
  private val scopeVariablesLink = Link("Scope Variables", scopeVariablesLocation.toUrl())

  override def title = "Errand Web Framework"
}