/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package forms

import xml.{Text, Attribute, Null, Elem}

/**
 * A behavior that sets the cols and rows attributes of a textarea.
 */
class TextareaSizeBehavior(cols: Int, rows: Int) extends Behavior {

  def transform(component: Component, elem: Elem) = {
    elem %
      Attribute (None, "cols", Text(cols.toString), Null) %
      Attribute (None, "rows", Text(rows.toString), Null)
  }
}

object TextareaSizeBehavior {

  def apply(cols: Int, rows: Int) = new TextareaSizeBehavior(cols, rows)
}