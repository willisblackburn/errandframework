/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import javax.servlet.http.Part
import org.errandframework.util.Log

/**
 * FileParameter is a RequestParameter that reads Part instances from a multipart request.
 * It is mostly used to back FileField.
 */
class FileParameter(val name: String) extends RequestParameter[Seq[Part]] {

  import FileParameter.log

  def decode() = {
    // Don't just check the name but the size too; apparently file fields with no files selected come back as
    // zero-byte files, at least in Tomcat 7.
    val parts = RequestContext.request.parts.filter(part => part.getName == name && part.getSize > 0)
    val value = Valid(parts)
    log.debug("Decoded FileParameter: " + name + ": " + value)
    value
  }

  def encode(value: Seq[Part]) =
    throw new UnsupportedOperationException("FileParameter does not support encode (files must be sent as attachments)")

  def encodeAsString(value: Seq[Part]) =
    throw new UnsupportedOperationException("FileParameter does not support encodeAsString (files must be sent as attachments)")

  override def toString() = "FileParameter(name=" + name + ")"
}

object FileParameter extends Log {

  def apply(): FileParameter = apply(RequestParameter.newName)

  def apply(name: String): FileParameter = new FileParameter(name)
}
