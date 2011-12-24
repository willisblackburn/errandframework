/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import java.util.UUID
import xml.{Attribute, Null, Text, Elem}

/**
 * IdBehavior is a Behavior that provides an identifier.  It is a distinct class so that other code can
 * recognize it and retrieve the identifier.
 */
class IdBehavior(val id: String) extends Behavior {
  
  def transform(component: Component, elem: Elem) = {
    elem % Attribute("id", Text(id), Null)
  }
}

object IdBehavior {

  def apply(id: String = UUID.randomUUID.toString) = new IdBehavior(id)
}