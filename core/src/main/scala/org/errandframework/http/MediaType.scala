/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * Case class representing a media type used in the HTTP Accept and Content-Type headers.
 */
case class MediaType(name: String, subtypeName: String, parameters: (String, String)*) {

  lazy val q: Double = parameters.find(p => p._1 == "q").map(p => p._2.toDouble).getOrElse(1.0)

  /**
   * Scala creates a copy method for case classes, except not when the class takes a variable number of parameters,
   * because varargs methods can't have default values.
   * Create a copy method that I can use that doesn't take varargs.
   * To change the parameters use withParameters.
   */
  def copy(name: String = this.name, subtypeName: String = this.subtypeName) =
    MediaType(name, subtypeName, parameters: _*)

  def copyWithParameters(parameters: (String, String)*) = MediaType(name, subtypeName, parameters: _*)

  /**
   * Creates a new MediaType with the given parameters added to the existing ones.
   */
  def appendParameters(parameters: (String, String)*) =
    MediaType(name, subtypeName, this.parameters ++ parameters: _*)

  def replaceParameters(parameters: (String, String)*) =
    MediaType(name, subtypeName, Map((this.parameters ++ parameters): _*).toSeq: _*)

  /**
   * Checks if this content type matches the given acceptable type.
   * The acceptable type may contain a wildcard for the subtype or both the subtype and the supertype.
   * According to the HTTP spec, if supertype is *, then subtype must also be *.
   * It doesn't specifically say that parameters are only permitted for non-wildcard types, but that
   * seems to make sense.
   */
  def matchesAccept(accept: MediaType) = accept.name == "*" ||
          accept.name == name && (accept.subtypeName == "*" ||
          accept.subtypeName == subtypeName && (accept.parameters.isEmpty || accept.parameters.sameElements(parameters)))

  /**
   * Returns this MediaType as a string in a format suitable for use in an HTTP Content-Type header.
   */
  override def toString() = name + "/" + subtypeName + ("" /: parameters)((s, p) => s + "; " + p._1 + "=" + p._2)
}

object MediaType {

  private val token = "([\\p{Alnum}!#$%&'*+\\-?@^_.]+)"
  private val mediaTypeRegex = (token + "/" + token + "(;[^,]*)?").r
  private val parameterRegex = (token + "=" + token).r

  def parseMediaTypes(mediaTypes: String): Seq[MediaType] =
    for (m <- mediaTypeRegex.findAllIn(mediaTypes).matchData.toSeq) yield apply(m.matched)

  /**
   * Builds a MediaType instance from a string.
   */
  implicit def apply(s: String) = {
    val mediaTypeRegex(name, subtypeName, parameters) = s
    new MediaType(name, subtypeName, (if (parameters != null) decodeParameters(parameters) else Seq.empty): _*)
  }

  private def decodeParameters(parameters: String): Seq[(String, String)] =
    for (m <- parameterRegex.findAllIn(parameters).matchData.toSeq) yield decodeParameter(m.matched)

  private def decodeParameter(parameter: String) = {
    val parameterRegex(name, value) = parameter
    (name, value)
  }
}