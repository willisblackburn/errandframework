/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import pages.Page
import org.errandframework.http._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * Tests the message resolution logic.
 */
@RunWith(classOf[JUnitRunner])
class MessageResolverTest extends WordSpec with MustMatchers with MockitoSugar {

  private val httpRequest = mock[HttpServletRequest]
  private val httpResponse = mock[HttpServletResponse]
  private val servlet = new TestServlet
  private val servlet2 = new TestServlet2
  private val requestContext = new RequestContext(httpRequest, httpResponse, servlet)
  private val requestContext2 = new RequestContext(httpRequest, httpResponse, servlet2)
  private val page = new TestPage
  private val page2 = new TestPage2
  private val derivedPage = new TestDerivedPage

  "MessageResolver.getMessage" should {

    "prefer to return a message from the servlet message resolver" in {
      RequestContext.withValue(Some(requestContext)) {
        Page.withValue(Some(page)) {
          val t1 = MessageResolver.getMessage("t1")
          System.out.println("t1 = " + t1)
          t1 must equal ("t1-servlet")
        }
      }
    }

    "fall back to retrieving the message from the page bundle" in {
      RequestContext.withValue(Some(requestContext)) {
        Page.withValue(Some(page)) {
          val t2 = MessageResolver.getMessage("t2")
          System.out.println("t2 = " + t2)
          t2 must equal ("t2-page")
        }
      }
    }

    "accept an additional bundle name" in {
      RequestContext.withValue(Some(requestContext)) {
        Page.withValue(Some(page)) {
          val t3 = MessageResolver.getMessage("org.errandframework.components.TestComponent", "t3")
          System.out.println("t3 = " + t3)
          t3 must equal ("t3-component")
        }
      }
    }

    "ignore any bundles that don't exist" in {
      RequestContext.withValue(Some(requestContext2)) {
        Page.withValue(Some(page)) {
          // Here we're using a RequestContext that references servlet2 (instanceof TestServlet2), which has
          // no resource bundle, so t1 should come out of the page bundle.
          val t1 = MessageResolver.getMessage("t1")
          System.out.println("t1 = " + t1)
          t1 must equal ("t1-page")
        }
      }
    }

    "fall back to a superclass bundle if the key doesn't exist in the class bundle" in {
      RequestContext.withValue(Some(requestContext)) {
        Page.withValue(Some(derivedPage)) {
          // TestDerivedPage has no bundle, so we should find t2 in the bundle for the parent Page class.
          val t2 = MessageResolver.getMessage("t2")
          System.out.println("t2 = " + t2)
          t2 must equal ("t2-page")
        }
      }
    }

    "fall back on a shorter key by removing dotted sections from the input key" in {
      RequestContext.withValue(Some(requestContext)) {
        Page.withValue(Some(page)) {
          val t1a = MessageResolver.getMessage("t1.a")
          System.out.println("t1a = " + t1a)
          t1a must equal ("t1-servlet")
          val t1ab = MessageResolver.getMessage("t1.a.b")
          System.out.println("t1ab = " + t1ab)
          t1ab must equal ("t1-servlet")
        }
      }
    }
  }
}

class TestServlet extends DefaultErrandServlet

class TestServlet2 extends DefaultErrandServlet

class TestPage extends Page {
  def bodyContent = Component.empty
}

class TestPage2 extends Page {
  def bodyContent = Component.empty
}

class TestDerivedPage extends TestPage
