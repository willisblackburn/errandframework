package org.errandframework.examples.features

import org.errandframework.components.Link
import org.errandframework.http._

import FeaturesExampleApplication._
import java.util.Date
import java.text.SimpleDateFormat

/**
 * A sample page that might run a search and display some results.
 * This page demonstrates required and optional parameters.
 */
class SearchPage extends FeaturePage {

  import SearchPage._

  def featureContent = {
    <p>This location of this page is defined in terms of the {Link("URLs Page Location", urlsLocation.toUrl()).render} URL page location.</p>
    <p>Your query: {q.get} (since {since.get.map(_.toString).getOrElse("any date")})</p>
    <p>Displaying page: {effectivePage}</p>
    <p>{Link("Next Page", searchLocation.toUrl(q, page -> Some(effectivePage + 1))).render}</p>
  }

  private def effectivePage = page.get.getOrElse(1)

  override def title = "Search"

  /**
   * We have to tell PageController (which is a subclass of PageProcessor) what parameters we need.
   * Using PageProcessor is not mandatory.  If the application needs more fine-grained control over parameter,
   * processing, it can use the decode method of Parameter rather than get.
   */
  override val parameters = Seq(q, page)
}

object SearchPage {

  /**
   * A String parameter using the default String codec.
   */
  val q = Parameter[String]("q")

  /**
   * An optional page parameter.
   * Regular Parameter instances require that the given request parameter be included in the request.
   * Use OptionParameter if it's okay for the parameter not to be present.  The parameter will be decoded into
   * Some(value) if the value was supplied and None if it wasn't.
   */
  val page = OptionParameter[Int]("page")

  /**
   * An optional parameter that uses a specific codec rather than one of the implicit ones in the http package.
   */
  val since = OptionParameter[Date]("since")(new DateCodec(new SimpleDateFormat("yyyy-MM-dd")))
}


