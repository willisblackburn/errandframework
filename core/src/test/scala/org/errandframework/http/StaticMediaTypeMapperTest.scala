/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.mock.MockitoSugar
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * Tests the StaticMediaTypeMapper class.
 */
@RunWith(classOf[JUnitRunner])
class StaticMediaTypeMapperTest extends WordSpec with MustMatchers with MockitoSugar {

  private val mapper = new StaticMediaTypeMapper(
    MediaType("text/plain") -> Seq("txt"),
    MediaType("text/html") -> Seq("html", "htm"),
    MediaType("image/jpeg") -> Seq("jpeg", "jpg", "jpe"),
    MediaType("image/gif") -> Seq("gif"),
    MediaType("application/x-errand-octet-stream") -> Seq("errand", "dat"),
    MediaType("application/octet-stream") -> Seq("dat")
  )

  "StaticMediaTypeMapper.getExtension" should {

    "map a MediaType to the highest-priority extension" in {
      mapper.getExtension(MediaType("text/plain")) must equal (Some("txt"))
      mapper.getExtension(MediaType("image/jpeg")) must equal (Some("jpeg"))
      mapper.getExtension(MediaType("application/octet-stream")) must equal (Some("dat"))
      mapper.getExtension(MediaType("application/x-errand-octet-stream")) must equal (Some("errand"))
    }
  }

  "StaticMediaTypeMapper.getMediaTypeForExtension" should {

    "map an extension to the most-appropriate media type based on extension priorites" in {
      mapper.getMediaTypeForExtension(Some("txt")) must equal (MediaType("text/plain"))
      mapper.getMediaTypeForExtension(Some("jpg")) must equal (MediaType("image/jpeg"))
      mapper.getMediaTypeForExtension(Some("jpeg")) must equal (MediaType("image/jpeg"))
      mapper.getMediaTypeForExtension(Some("dat")) must equal (MediaType("application/octet-stream"))
      mapper.getMediaTypeForExtension(Some("errand")) must equal (MediaType("application/x-errand-octet-stream"))
    }
  }

}