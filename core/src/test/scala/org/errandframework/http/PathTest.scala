/*
* Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
*/

package org.errandframework.http

import org.scalatest.matchers.MustMatchers
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * Tests the Path class.
 */
@RunWith(classOf[JUnitRunner])
class PathTest extends WordSpec with MustMatchers {

  val names = "path" :: "to" :: "file.dat" :: Nil

  "Path" should {

    "construct a path from a list of names" in {
      val path = Path(names)
      path.names must equal (names)
    }

    "construct a path from a String" in {
      Path("path/to/file.dat").names must equal (names)
    }

    "ignore any empty segments in the path" in {
      Path("/path/to/file.dat").names must equal (names)
      Path("path///to/file.dat").names must equal (names)
    }

    "convert a path to a String" in {
      Path(names).toString must equal ("path/to/file.dat")
    }

    "convert an empty path into an empty String and vice-versa" in {
      Path(Nil).toString must equal ("")
      Path("").names must equal (Nil)
    }

    "support equality" in {
      // Identity
      val path = Path(names)
      path must equal (path)
      // Equality with another path constructed from the same list
      path must equal (Path(names))
      // Equality with another path constructed from a different, but equal, list
      path must equal (Path("path" :: "to" :: "file.dat" :: Nil))
    }

    "implement hashCode in terms of the list of names" in {
      Path(names).hashCode must equal (names.hashCode)
    }

    "generate a new path by appending a second path to itself" in {
      (Path(names) / Path("second" :: "part" :: Nil)).names must equal (names ::: "second" :: "part" :: Nil)
    }

    "remove a base path" in {
      (Path(names) </ Path(names ::: "second" :: "part" :: Nil)).names must equal ("second" :: "part" :: Nil)
    }

    "provide access to the directory part of the path" in {
      Path(names).directory must equal (Path("path" :: "to" :: Nil))
      Path("path" :: Nil).directory must equal (Path.empty)
      Path.empty.directory must equal (Path.empty)
    }

    "provide access to the name part of the path" in {
      Path(names).name must equal ("file.dat")
    }

    "provide access to the extension part of the name" in {
      Path(names).extension must equal (Some("dat"))
      Path("no extension" :: Nil).extension must equal (None)
    }
  }
}
