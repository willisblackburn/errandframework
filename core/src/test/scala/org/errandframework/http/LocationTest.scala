package org.errandframework.http

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

/**
 * Tests the methods of the Location class.
 */
@RunWith(classOf[JUnitRunner])
class LocationTest extends WordSpec with MustMatchers with MockitoSugar {

  private val httpResponse = mock[HttpServletResponse]
  private val servlet = new DefaultErrandServlet

  "Location" should {

    "generate a URL containing both the context and servlet path" in {
      val httpRequest = mock[HttpServletRequest]
      when(httpRequest.getContextPath).thenReturn("/context")
      when(httpRequest.getServletPath).thenReturn("/servlet")
      RequestContext.withValue(Some(new RequestContext(httpRequest, httpResponse, servlet))) {
        val location = rootLocation / "test"
        location.toUrl() must equal ("/context/servlet/test")
      }
    }

    "generate a URL with just the context path if the servlet path is empty" in {
      val httpRequest = mock[HttpServletRequest]
      when(httpRequest.getContextPath).thenReturn("/context")
      when(httpRequest.getServletPath).thenReturn("")
      RequestContext.withValue(Some(new RequestContext(httpRequest, httpResponse, servlet))) {
        val location = rootLocation / "test"
        location.toUrl() must equal ("/context/test")
      }
    }

    "generate a URL containing just the servlet path if the context path is empty" in {
      val httpRequest = mock[HttpServletRequest]
      when(httpRequest.getContextPath).thenReturn("")
      when(httpRequest.getServletPath).thenReturn("/servlet")
      RequestContext.withValue(Some(new RequestContext(httpRequest, httpResponse, servlet))) {
        val location = rootLocation / "test"
        location.toUrl() must equal ("/servlet/test")
      }
    }

    "generate a URL without the context and servlet paths if they are both empty" in {
      val httpRequest = mock[HttpServletRequest]
      when(httpRequest.getContextPath).thenReturn("")
      when(httpRequest.getServletPath).thenReturn("")
      RequestContext.withValue(Some(new RequestContext(httpRequest, httpResponse, servlet))) {
        val location = rootLocation / "test"
        location.toUrl() must equal ("/test")
      }
    }
  }
}