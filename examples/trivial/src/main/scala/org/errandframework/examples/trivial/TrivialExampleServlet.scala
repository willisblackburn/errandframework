package org.errandframework.examples.trivial

import org.errandframework.http._
import org.errandframework.http.Location.rootLocation

/**
 * Servlet for Trivial Example.
 */
class TrivialExampleServlet extends ErrandServlet {

  private val welcomeLocation = rootLocation / "welcome"

  private val mapping: PartialFunction[Path, Controller] = {
    case welcomeLocation() => XhtmlResponse(<html xmlns="http://www.w3.org/1999/xhtml"><h1>Welcome to Errand</h1><p>The trival example works!</p></html>)
  }

  protected val mappers = Seq(new PathRequestMapper(mapping))
}