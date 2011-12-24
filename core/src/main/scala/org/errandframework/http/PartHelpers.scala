/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import javax.servlet.http.Part

/**
 * Helper methods for dealing with Java Servlet 3.0 multipart form parts.
 */
object PartHelpers {

  /**
   * Regex to find the part's file name.
   * There is a "filename=" parameter somewhere in the content-disposition string.
   * The name to the right of the equals might have quotes around it or might not, so handle both cases.
   */
  private val fileNameRegex = ".*filename=\"?((?<=\")[^\"]+(?=\")|[^;\"][^;]+).*".r

  def getFileName(part: Part): Option[String] = {
    part.getHeader("content-disposition") match {
      case null => None
      case contentDisposition =>
        try {
          val fileNameRegex(fileName) = contentDisposition
          Some(fileName)
        } catch {
          case e: MatchError => None
        }
    }
  }
}