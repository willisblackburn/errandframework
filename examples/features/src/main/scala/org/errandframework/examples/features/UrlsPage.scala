package org.errandframework.examples.features

import org.errandframework.components.pages.Page
import org.errandframework.components.Link
import org.errandframework.http._

import FeaturesExampleApplication._
import java.util.Date

/**
 * A page that demonstrates different ways of using URLs, paths, and parameters.
 */
class UrlsPage extends FeaturePage {

  def featureContent = {
    <ul>
      <li>A URL generated from a path: {Link(pathUrl).render}</li>
      <li>A URL generated from a location: {Link(locationUrl).render}</li>
      <li>A URL with a parameter embedded in the URL: {Link(parameterUrl).render}</li>
      <li>A URL with an optional Int parameter: {Link(intParameterUrl).render}</li>
      <li>A URL with an optional Date parameter: {Link(dateParameterUrl).render}</li>
      <li>A link to the current location: {Link(Location.thisLocation.toUrl()).render}</li>
    </ul>
  }

  override def title = "URLs, Paths, and Parameters"

  private def pathUrl = (rootPath / "this" / "path" / "does" / "not" / "work").toString

  private def locationUrl = urlsLocation.toUrl()

  private def parameterUrl = searchLocation.toUrl(SearchPage.q -> "errand framework")

  // For the Int and Date parameters, we supply actual Int and Date parameters when generating the URL.
  // As long as we use the same Parameter both to make the URL and to decode the request parameter, we can
  // just deal with normal types and not worry that everything needs to be a string in HTTP.

  private def intParameterUrl = searchLocation.toUrl(SearchPage.q -> "errand framework",
    SearchPage.page -> Some(5))

  private def dateParameterUrl = searchLocation.toUrl(SearchPage.q -> "errand framework",
    SearchPage.since -> Some(new Date(System.currentTimeMillis - 86400000)), SearchPage.page -> Some(5))
}

