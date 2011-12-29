package org.errandframework.examples.features

import org.errandframework.http._

/**
 * In most applications there will be an object that defines global, common constructs:  locations, database
 * connections, etc.
 */
object FeaturesExampleApplication {

  // Define all the locations for this application.  All references to internal URLs are in terms of these
  // locations, so if we want to change the URL structure of the site, we can just change the locations.

  val urlsLocation = rootLocation / "urls"
  val searchLocation = urlsLocation / "search" / SearchPage.q
  val linksLocation = rootLocation / "links"
  val links2Location = rootLocation / "links2"
  val formsLocation = rootLocation / "forms"
}