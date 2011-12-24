/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import xml.NodeSeq
import javax.servlet.http.HttpServletResponse.SC_OK

/**
 * XhtmlResponse represents a response that consists of XHTML.
 * Normally XHTML should be served as application/xhtml+xml.
 * But a certain browser from a certain large software company that is unfortunately in widespread use doesn't
 * understand application/xhtml+xml, so in those cases, we want to send text/html instead.
 * Fortunately browsers that do understand application/xhtml+xml advertise that fact through the HTTP Accept
 * header, so we can make things work for the unmentioned large company browser without having to refer to
 * it by name.
 */
class XhtmlResponse(xhtml: NodeSeq, headers: (String, String)*) extends NegotiatedResponse {

  val status = SC_OK
  val mediaType = None

  val xhtmlMediaType = MediaType("application", "xhtml+xml", "q" -> "1.0")
  val htmlMediaType = MediaType("text", "html", "q" -> "0.1")

  override def haveMediaTypes = Seq(xhtmlMediaType, htmlMediaType)

  override def responseForMediaType(mediaType: MediaType) = {
    // Modern browsers recognize "<!DOCTYPE html>" as HTML5.
    // We always send HTML5 using the XML encoding, which we specify using the content type.
    CharacterBufferResponse(mediaType, "<!DOCTYPE html>\n" + xhtml.toString, headers: _*)
  }
}

object XhtmlResponse {

  def apply(xhtml: NodeSeq, headers: (String, String)*) = new XhtmlResponse(xhtml, headers: _*)
}
