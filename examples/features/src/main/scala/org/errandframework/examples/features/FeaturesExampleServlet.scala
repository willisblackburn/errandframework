package org.errandframework.examples.features

import org.errandframework.http._
import org.errandframework.components.pages.PageController

import FeaturesExampleApplication._

/**
 * Servlet for Trivial Example.
 */
class FeaturesExampleServlet extends DefaultErrandServlet {

  private val mapper = new PathRequestMapper({
    // For HomePage we render the same singleton instance for everybody.  HomePage therefore must not have have any
    // user- or session-specific data.  It could, however, refer to a SessionVar or otherwise generate different HTML,
    // each time it is rendered.  It's not a static page.
    case rootLocation() => new PageController(HomePage)
    // We create all other pages on demand.  No reason it has to be exactly this way.  The program is just
    // demonstrating different strategies.
    case linksLocation() => new PageController(new LinksPage("the links URL"))
    case links2Location() => new PageController(new LinksPage("the links2 URL"))
    case urlsLocation() => new PageController(new UrlsPage)
    case searchLocation() => new PageController(new SearchPage)
    case formsLocation() => new PageController(new FormsPage)
  })

  override protected val mappers = Seq(mapper, defaultMapper)
}
