/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import xml.{Text, Null, Attribute, Elem}
import org.errandframework.http.Codec

/**
 * Behaviors are used to transform components after they have been rendered.
 */
trait Behavior {

  def transform(component: Component, elem: Elem): Elem
}

object Behavior {

  implicit def fromFunction(f: Elem => Elem) = new Behavior {
    def transform(component: Component, elem: Elem) = f(elem)
  }

  implicit def fromAttribute(attribute: Attribute) = new Behavior {
    def transform(component: Component, elem: Elem) = elem % attribute
  }

  def attribute(key: String, value: String) = new Behavior {
    def transform(component: Component, elem: Elem) = elem % Attribute(key, Text(value), Null)
  }

  def appendAttribute(key: String, value: String, separator: String) = new Behavior {
    def replacementValue(elem: Elem) = elem.attribute(key) match {
      case Some(attribute) => attribute ++ Text(separator + value)
      case _ => Text(value)
    }
    def transform(component: Component, elem: Elem) = elem % Attribute(key, replacementValue(elem), Null)
  }

  def cssStyle(cssStyle: String) = attribute("style", cssStyle)

  def appendCssStyle(cssStyle: String) = appendAttribute("style", cssStyle, "  ")

  def cssClass(cssClass: String) = attribute("class", cssClass)

  def appendCssClass(cssClass: String) = appendAttribute("class", cssClass, " ")

  def name(name: String) = attribute("name", name)

  def value[T](value: T)(implicit codec: Codec[T]) = attribute("value", codec.encode(value))

  def rel(name: String) = attribute("rel", name)

  def rev(name: String) = attribute("rev", name)

  def transform(elem: Elem, behaviors: Behavior*): Elem = (elem /: behaviors)((e, behavior) => behavior.transform(elem, e))
}
