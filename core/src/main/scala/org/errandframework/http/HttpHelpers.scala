/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import java.lang.Math._
import java.net.URLEncoder

/**
 * Helper methods for dealing with the HTTP protocol.
 * The methods of this class are low-level and do not depend on either the R or S objects.
 */
object HttpHelpers {

  def acceptedContentTypes(headers: Seq[String], haves: Seq[MediaType]): Seq[MediaType] = {

    // If the browser did not send any headers, then assume that it accepts anything.

    val accepted = if (headers.isEmpty)
      haves
    else {

      // Break up the accept list.
      val accepts = headers.flatMap(MediaType.parseMediaTypes)

      // Compare the acceptable MediaType instances with what the server has and return the intersection.
      for (have <- haves; accept <- accepts if have.matchesAccept(accept))
        yield have.replaceParameters("q" -> min(have.q, accept.q).toString)
    }

    // Finally sort the accepted types by q and return.
    accepted.sortWith(_.q > _.q)
  }

  def appendUrlParameters(url: String, parameters: (String, String)*): String = {
    val queryString = parameters.map(p => p._1 + "=" + URLEncoder.encode(p._2, "UTF-8")).mkString("&")
    appendUrlParameters(url, queryString)
  }

  def appendUrlParameters(url: String, queryString: String): String = {
    if (queryString.length == 0)
      url
    else {
      val q = url.indexOf('?')
      url + (if (q == -1) "?" else if (q < url.length - 1) "&" else "") + queryString
    }
  }
}
