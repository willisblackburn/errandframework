/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package forms

import org.errandframework.http.RequestParameter._
import org.errandframework.http.Valid._
import org.errandframework.http.{ValueOperations, Valid, Value, RequestParameter}

/**
 * An Editor is a UI component that enables a user to make changes to an object of some type T.
 * Field is a subclass of Editor and represents editors that are implemented as an HTML control.  However editors
 * may be more complex and may include other Editors.
 */
abstract class Editor[T] extends Component with ValueOperations[T] {

  def fields: Seq[Field[_]]

  def validate(): Boolean = true

  def modelGet(): T

  def modelSet(value: T): Unit

  /**
   * Sets the model value to the value from the parameter.
   * Works for any Editor without the caller having to worry about what type T is.
   */
  def updateModel() {
    modelSet(get)
  }
}
