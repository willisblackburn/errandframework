/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.util

import xml._
import util.matching.Regex

/**
 * Some utility methods for working with Scala's XML classes.
 */
object XmlHelpers {

  def asUnparsed(any: Any) = Unparsed(any.toString)

  def asUnparsed(anyOption: Option[Any]): Option[Unparsed] = anyOption.map(asUnparsed)

  def asText(any: Any) = Text(any.toString)

  def asText(anyOption: Option[Any]): Option[Text] = anyOption.map(asText)

  case class AllowTag(label: String, attributes: (String, Regex)*)

  def cleanAttributes(attributes: MetaData, allowedAttributes: (String, Regex)*): MetaData = {
    (Null.asInstanceOf[MetaData] /: attributes) {
      (cleanAttributes: MetaData, md: MetaData) => md match {
        case attribute: Attribute => allowedAttributes.find(_._1 == attribute.key).map(_._2) match {
          case Some(regex) => md.value.text match {
            case regex() => Attribute(attribute.key, attribute.value, cleanAttributes)
            case _ => cleanAttributes
          }
          case _ => cleanAttributes
        }
        case _ => cleanAttributes
      }
    }
  }

  def clean(xml: NodeSeq, allowed: AllowTag*): NodeSeq = {

    val allowedMap = allowed.map(a => (a.label, a.attributes)).toMap

    // Check each tag and see if it's in the allowed set.  If it is, then keep it, otherwise remove it.
    xml flatMap {
      case elem: Elem => allowedMap.get(elem.label) match {
        case Some(allowedAttributes) => new Elem(elem.prefix, elem.label, cleanAttributes(elem.attributes, allowedAttributes: _*),
          TopScope, clean(elem.child, allowed: _*): _*)
        case _ => Seq.empty
      }
      case text: Text => text
      case _ => Seq.empty
    }
  }

  def clean(s: String, allowed: AllowTag*): String = {

    // Wrap the input in a dummy tag because loadString only returns Elem.
    val xml = XML.loadString("<top>" + s + "</top>").child
    if (allowed.isEmpty)
      xml.toString
    else
      XmlHelpers.clean(xml, allowed: _*).toString
  }
}

