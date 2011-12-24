/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.util

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import xml.NodeSeq
import org.errandframework.util.XmlHelpers.AllowTag
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * Tests for XmlHelpers methods.
 */
@RunWith(classOf[JUnitRunner])
class XmlHelpersTest extends WordSpec with MustMatchers {

  val allowed = Seq(AllowTag("html", "a" -> "http:.*".r, "id" -> ".*".r), AllowTag("body"), AllowTag("p"))

  "XmlHelpers.clean" should {

    "remove any XML tag that is not part of the allowed set" in {
      val output = XmlHelpers.clean(<html><head>head</head><body><p>some text</p>more text</body></html>, allowed: _*)
      val expected = NodeSeq.fromSeq(<html><body><p>some text</p>more text</body></html>)
      output must equal (expected)
    }

    "remove any XML attribute that is not part of the allowed set" in {
      val output = XmlHelpers.clean(<html id="xyz" href="javascript:foo()" onclick="remove me"/>, allowed: _*)
      val expected = NodeSeq.fromSeq(<html id="xyz"/>)
      output must equal (expected)
    }

    "convert an input string to XML before cleaning, then convert the result back" in {
      val output = XmlHelpers.clean("""<html id="xyz" href="javascript:foo()" onclick="remove me"/>""", allowed: _*)
      val expected = """<html id="xyz"></html>"""
      output must equal (expected)
    }
  }
}