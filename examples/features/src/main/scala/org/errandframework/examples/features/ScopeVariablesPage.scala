package org.errandframework.examples.features

import org.errandframework.components.ActionLink
import org.errandframework.http.{RedirectResponse, ApplicationVar, SessionVar, RequestVar}

import FeaturesExampleApplication._

/**
 * Demonstrates the various scope variables:  RequestVar, SessionVar, and ApplicationVar.
 */
class ScopeVariablesPage extends FeaturePage {

  import ScopeVariablesPage._

  def featureContent = {
    <div>
      <p>RequestVar value: {r.value} {rincLink.render}</p>
      <p>SessionVar value: {s.value} {sincLink.render}</p>
      <p>ApplicationVar value: {a.value} {aincLink.render}</p>
    </div>
  }

  private val rincLink = ActionLink("Increment") { r.value += 1; RedirectResponse(scopeVariablesLocation.toUrl()) }
  private val sincLink = ActionLink("Increment") { s.value += 1; RedirectResponse(scopeVariablesLocation.toUrl()) }
  private val aincLink = ActionLink("Increment") { a.value += 1; RedirectResponse(scopeVariablesLocation.toUrl()) }

  override def title = "Scope Variables"
}

object ScopeVariablesPage {

  private val r = RequestVar[Int](0)
  private val s = SessionVar[Int](0)
  private val a = ApplicationVar[Int](0)
}