/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import org.errandframework.util.XmlHelpers.AllowTag
import xml.{XML, NodeSeq}
import org.errandframework.util.XmlHelpers
import xml.NodeSeq._

/**
 * CleanXmlCodec is a codec for Strings that converts the input to XML and removes any disallowed tags.
 */
class CleanXmlCodec(allowed: AllowTag*) extends Codec[String] {

  def encode(value: String) = value.toString

  def decode(raw: String) = XmlHelpers.clean(raw, allowed: _*)
}

