/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.Part
import org.mockito.Mockito.when
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * Test PartHelpers.
 */
@RunWith(classOf[JUnitRunner])
class PartHelpersTest extends WordSpec with MustMatchers with MockitoSugar {

  "PartHelpers.getFileName" should {

    "return the part file name from the content-disposition field" in {
      val part = mock[Part]
      when(part.getHeader("content-disposition")) thenReturn "attachment; filename=file.dat"
      PartHelpers.getFileName(part) must equal (Some("file.dat"))
    }

    "return the part file name from the content-disposition field (even with no spaces after semicolons)" in {
      val part = mock[Part]
      when(part.getHeader("content-disposition")) thenReturn "attachment;filename=file.dat"
      PartHelpers.getFileName(part) must equal (Some("file.dat"))
    }

    "return the part file name from the content-disposition field (even with other parameters present)" in {
      val part = mock[Part]
      when(part.getHeader("content-disposition")) thenReturn "attachment; filename=file.dat; modification-date=\"Wed, 12 February 1997 16:29:51 -0500\""
      PartHelpers.getFileName(part) must equal (Some("file.dat"))
    }

    "return the part file name from the content-disposition field (when the name is surrounded by quotes)" in {
      val part = mock[Part]
      when(part.getHeader("content-disposition")) thenReturn "attachment; filename=\"file.dat;10\"; modification-date=\"Wed, 12 February 1997 16:29:51 -0500\""
      PartHelpers.getFileName(part) must equal (Some("file.dat;10"))
    }

    "return None if the part has no content-disposition header" in {
      val part = mock[Part]
      when(part.getHeader("content-disposition")) thenReturn null
      PartHelpers.getFileName(part) must equal (None)
    }

    "return None if there is no filename parameter" in {
      val part = mock[Part]
      when(part.getHeader("content-disposition")) thenReturn "attachment; modification-date=\"Wed, 12 February 1997 16:29:51 -0500\""
      PartHelpers.getFileName(part) must equal (None)
    }
  }
}