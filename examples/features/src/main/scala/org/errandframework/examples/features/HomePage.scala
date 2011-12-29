package org.errandframework.examples.features

import org.errandframework.components.pages.Page

import FeaturesExampleApplication._
import org.errandframework.components.{MessagePanel, Link}

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
    // Often we'll put a <div> or a <span> at the start of a component in order to gather all the XHTML
    // into one lexical block.  Otherwise we have to alternate between using {} and ++ to mix the XHTML and Scala
    // code.  For a demonstration, try removing the initial <div> and notice you have to remove {} around
    // messagePanel.render and add ++.
    <div>
      <h1>Welcome to Errand Web Framework</h1>
      {messagePanel.render}
      <h2>Features</h2>
      <ul>
        <li>{linksLink.render}</li>
        <li>{urlsLink.render}</li>
        <li>{formsLink.render}</li>
        <ul>
          <li>{simpleFormLink.render}</li>
          <li>{actionFormLink.render}</li>
          <li>{complexActionFormLink.render}</li>
        </ul>
        <li>{repeatersLink.render}</li>
        <li>{scopeVariablesLink.render}</li>
        <li>{ajaxLink.render}</li>
      </ul>
    </div>
  }

  private val linksLink = Link("Links", linksLocation.toUrl())
  private val urlsLink = Link("URLs, Paths, and Parameters", urlsLocation.toUrl())
  private val formsLink = Link("Forms", formsLocation.toUrl())
  private val simpleFormLink = Link("Simple Form", simpleFormLocation.toUrl())
  private val actionFormLink = Link("ActionForm", actionFormLocation.toUrl())
  private val complexActionFormLink = Link("Complex ActionForm", complexActionFormLocation.toUrl())
  private val repeatersLink = Link("Repeaters", repeatersLocation.toUrl())
  private val scopeVariablesLink = Link("Scope Variables", scopeVariablesLocation.toUrl())
  private val ajaxLink = Link("AJAX", ajaxLocation.toUrl())

  private val messagePanel = new MessagePanel

  override def title = "Errand Web Framework"
}