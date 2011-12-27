package org.errandframework.examples.trivial

import org.errandframework.http._
import org.errandframework.components.Link

/**
 * Servlet for Trivial Example.
 */
class TrivialExampleServlet extends ErrandServlet {

  private val counterLocation = rootLocation / "counter"

  private val mapping: PartialFunction[Path, Controller] = {

    case rootLocation() => XhtmlResponse(
      <html xmlns="http://www.w3.org/1999/xhtml">
        <h1>Welcome to Errand</h1>
        <p>The trival example works!</p>
        <p>{Link("Counter", counterLocation.toUrl()).render}</p>
      </html>
    )

    case counterLocation() => counter.value += 1; XhtmlResponse(
      <html xmlns="http://www.w3.org/1999/xhtml">
        <h1>Counter</h1>
        <p>You've loaded this page {counter.value} times.</p>
      </html>
    )

  }

  protected val mappers = Seq(new PathRequestMapper(mapping))

  private val counter = SessionVar[Int](0)
}
