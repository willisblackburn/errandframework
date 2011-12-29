package org.errandframework.examples.features

import org.errandframework.components.ActionLink

import FeaturesExampleApplication._
import org.errandframework.http._

/**
 * Demonstrates the various scope variables:  RequestVar, SessionVar, and ApplicationVar.
 */
class ScopeVariablesPage extends FeaturePage {

  import ScopeVariablesPage._

  def featureContent = {
    <div>
      <p>No matter how many times you click "Increment," the RequestVar will always be zero, because it is reset
        to zero every time the page is loaded.</p>
      <p>RequestVar value: {r.value} {rincLink.render}</p>
      <p>The SessionVar will increase by one every time you click "Increment," but if you {clearSessionLink.render} or
        view the page from another browser, you will see that it resets back to zero.</p>
      <p>SessionVar value: {s.value} {sincLink.render}</p>
      <p>The ApplicationVar will increase by one every time you click "Increment," and the change will be visible
        to all other users.</p>
      <p>ApplicationVar value: {a.value} {aincLink.render}</p>
    </div>
  }

  private val rincLink = ActionLink("Increment") { r.value += 1; redirectHere }
  private val sincLink = ActionLink("Increment") { s.value += 1; redirectHere }
  private val aincLink = ActionLink("Increment") { a.value += 1; redirectHere }

  private val clearSessionLink = ActionLink("clear the session") {
    RequestContext.httpSession.invalidate()
    redirectHere
  }

  private val redirectHere = RedirectResponse(scopeVariablesLocation.toUrl())

  override def title = "Scope Variables"
}

object ScopeVariablesPage {

  private val r = RequestVar[Int](0)
  private val s = SessionVar[Int](0)
  private val a = ApplicationVar[Int](0)
}