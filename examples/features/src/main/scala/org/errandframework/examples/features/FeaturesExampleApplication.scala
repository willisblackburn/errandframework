package org.errandframework.examples.features

import org.errandframework.http._

/**
 * In most applications there will be an object that defines global, common constructs:  locations, database
 * connections, etc.
 *
 */
object FeaturesExampleApplication {

  val urlsLocation = rootLocation / "urls"
  val searchLocation = urlsLocation / "search" / SearchPage.q
  val linksLocation = rootLocation / "links"
  val links2Location = rootLocation / "links2"
  val formsLocation = rootLocation / "forms"
}