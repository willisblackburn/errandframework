package org.errandframework.examples.trivial

import org.errandframework.http._
import org.errandframework.http.Location.rootLocation
import org.errandframework.components.Link

/**
 * Servlet for Trivial Example.
 */
class TrivialExampleServlet extends ErrandServlet {

  private val welcomeLocation = rootLocation / "welcome"
  private val anotherLocation = rootLocation / "another"

  private val mapping: PartialFunction[Path, Controller] = {

    case welcomeLocation() => XhtmlResponse(
      <html xmlns="http://www.w3.org/1999/xhtml">
        <h1>Welcome to Errand</h1>
        <p>The trival example works!</p>
        <p>{Link("Second Page", anotherLocation.toUrl()).render}</p>
      </html>
    )

    case anotherLocation() => XhtmlResponse(
      <html xmlns="http://www.w3.org/1999/xhtml">
        <h1>Another Page</h1>
        <p>This is another page.</p>
      </html>
    )

  }

  protected val mappers = Seq(new PathRequestMapper(mapping))
}