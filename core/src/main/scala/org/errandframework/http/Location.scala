/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

object Location {

  sealed trait Segment
  case class NameSegment(name: String) extends Segment
  case class ParameterSegment(parameter: Parameter[_]) extends Segment

  def thisLocation = rootLocation / RequestContext.request.path

  /**
   * Allow the application to generate a location in the form Location("/path/to/something") or by
   * implicit conversion.
   */
  implicit def apply(path: Path) = rootLocation / path
}

import Location.{Segment, NameSegment, ParameterSegment}

// TODO, need to make sure we're using URLDecoder when appropriate.

/**
 * Encapsulates the concept of a location defined by a URL.
 * The constructor is private;  application should create locations by appending segments to rootLocation
 * using the '/' method.
 * Location uses List instead of Seq because it needs List to integrate well with Path.
 */
class Location private[http](segments: List[Segment]) {

  /**
   * Assemble parameters into a list in order to make it easy to do a contains check later.
   */
  private val parameters = segments collect { case s: ParameterSegment => s.parameter }

  def /(path: Path): Location = {
    val nameSegments = for (n <- path.names) yield NameSegment(n)
    new Location(segments ++ nameSegments)
  }

  def /(parameter: Parameter[_]): Location = new Location(segments :+ ParameterSegment(parameter))

  /**
   * Does the request path match the location path?
   * If so then set the parameter raw values from the path and return true.
   * Otherwise return false and leave the parameters alone.
   */
  def unapply(path: Path): Boolean = {

    val names = path.names

    // If the number of segments is zero, then the number of names must be zero.
    if (segments.isEmpty)
      return names.isEmpty

    // If the number of path segments exceeds the number of names in the path, then it cannot be a match.
    if (segments.length > names.length)
      return false

    // If the number of names is greater than the number of path segments, then collapse the rest of the names
    // back on to the last name. (If there are exactly as many names as segments, the logic will simply
    // reproduce the original names sequence.)
    val adjustedNames = if (segments.length == names.length)
      names
    else {
      val iterator = names.iterator
      iterator.take(segments.length - 1).toList :+ iterator.mkString("/")
    }

    val segmentsAndNames = segments.zip(adjustedNames)

    def matchSegment(s: (Segment, String)) = s._1 match {
      case NameSegment(n) => n == s._2
      case _ => true
    }

    // The request path matches if each of the names matches the corresponding element in the location path.
    // Only match up to the number of segments.
    // It's okay if the path from the request is longer than the location path as long as the last segment in the
    // location path is a parameter.  The parameter will receive the remainder of the request path.
    // If the last segment is not a parameter, then the match will fail, because the last name in adjustedNames
    // will have the rest of the path from the request.
    if (!segmentsAndNames.forall(segmentAndName => matchSegment(segmentAndName._1, segmentAndName._2)))
      return false

    def setParameterValue(s: (Segment, String)) = s._1 match {
      case ParameterSegment(p) => p.decodeRawAndSet(s._2)
      case _ => ()
    }

    // For each of the parameters that are in the in location path, substitute the value from the path for the raw
    // value that would have come from the request.
    segmentsAndNames.foreach(setParameterValue)

    true
  }

  // TODO, return Url?

  def toUrl(assignments: ParameterAssignment[_]*) =
    RequestContext.request.contextServletPath.toComponentString + toLocalUrl(assignments: _*)

  def toUrl(defaultParameters: Seq[RequestParameter[_]], assignments: ParameterAssignment[_]*): String = {
    toUrl((assignments.toSet ++ defaultParameters.map(_.toAssignment)).toSeq: _*)
  }

  /**
   * Generates a path given the parameter values.
   * The path is in "path info" format, without any context or servlet path.
   * Uses the current value for any parameter that the Location needs but that is not supplied by the caller.
   */
  def toLocalUrl(assignments: ParameterAssignment[_]*) = {
    // Split the given values into those that are part of the Location path and those that are extra.
    // The extra parameters will be added to the end of the path.
    val (pathAssignments, otherAssignments) = assignments.partition(v => parameters.contains(v.parameter))
    val encodedSegments = segments map {
      case NameSegment(n) => n
      case ParameterSegment(p) =>
        val a = pathAssignments.find(_.parameter == p).getOrElse(p.toAssignment)
        a.encodeAsString
    }
    val path = Path(encodedSegments)
    HttpHelpers.appendUrlParameters("/" + path.toString, otherAssignments.flatMap(_.encode).mkString("&"))
  }

  def toLocalUrl(defaultParameters: Seq[RequestParameter[_]], assignments: ParameterAssignment[_]*): String = {
    toLocalUrl((assignments.toSet ++ defaultParameters.map(_.toAssignment)).toSeq: _*)
  }
}
