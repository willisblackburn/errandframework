/*
* Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
*/

package org.errandframework.http

import collection.mutable.{ArraySeq, ListBuffer, Builder}
import collection.generic.{CanBuildFrom, HasNewBuilder, GenericTraversableTemplate}
import collection.SeqLike

// TODO, fix docs:

/**
 * <p>Path converts paths between Strings and a List of Strings (path components).
 * When converting from Strings to Lists:</p>
 * <ul>
 *   <li>An empty list becomes a blank string.</li>
 *   <li>Otherwise the string is split around paths separators ('/').</li>
 *   <li>Any zero-length path components are collapsed/removed, so the path "/foo//bar" becomes "foo" :: "bar" :: Nil.</li>
 * </ul>
 * <p>When converting from Lists to Strings:
 * <ul>
 *   <li>An empty string becomes an empty list.</li>
 *   <li>Otherwise the string consists of an initial path separator ('/')
 *   followed by the path components delimited by separators ('/').</li>
 *   <li>Any empty path components in the initial string are removed.</li>
 *   <li>The resulting path string does not end with a separator ('/').</li>
 * </ul>
 */
class Path(val names: List[String]) extends Seq[String] with SeqLike[String, Path] {

  def this(path: String) = this(path.split("/+").toList.filter(_.length > 0))

  // Seq implementation

  def length = names.length

  def apply(idx: Int) = names.apply(idx)

  def iterator = names.iterator

  override protected[this] def newBuilder = Path.newBuilder

  // New methods just for Path.

  def /(relativePath: Path) = new Path(names ::: relativePath.names)

  def </(relativePath: Path) = {
    if (relativePath.names.startsWith(names))
      new Path(relativePath.names.drop(names.length))
    else
      throw new IllegalArgumentException("Path " + this + " is not a base path of given path " + relativePath)
  }

  def directory = new Path(names.dropRight(1))

  // TODO, name should return Name.

  def name = names.last

  def components = {
    val n = name
    n.lastIndexOf('.') match {
      case -1 => (directory, n, None)
      case i => (directory, n.substring(0, i), Some(n.substring(i + 1)))
    }
  }

  def baseName = components._2

  def extension = components._3

  override def toString() = names.mkString("/")
}

object Path {

  val empty = new Path(Nil)

  implicit def apply(names: List[String]) = new Path(names)

  implicit def apply(path: String) = new Path(path)

  implicit def apply(c: Class[_]) = new Path(c.getName.split('.').toList)

  implicit def apply(pkg: Package) = new Path(pkg.getName.split('.').toList)

  def unapply(path: Path) = Some(path.names)

  def newBuilder = new ListBuffer[String] mapResult (result => new Path(result))

  implicit def canBuildFrom = new CanBuildFrom[Path, String, Path] {
    def apply(from: Path) = newBuilder
    def apply() = newBuilder
  }
}

