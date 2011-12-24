/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package forms

import org.errandframework.http.Value
import org.errandframework.util.Log
import xml.NodeSeq

// TODO, maybe LabeledField just displays the label and field together, and there is a separate renderer--perhaps
// part of this FormSection idea--that displays the label in a div.

class Label(val content: Component, val field: Field[_]) extends Component with Log {

  def render() = field.id match {
    case Some(id) => FormStyle.renderLabel(this)
    case _ =>
      log.warn("Field used with LabeledField does not have an ID: " + field)
      Seq.empty
  }
}

object Label {

  def apply[T](content: Component, field: Field[T], labelBehaviors: Behavior*) = new Label(content, field) {
    override def behaviors = labelBehaviors
  }
}

/**
 * LabeledField displays a field with a label.
 * It provides an HTML &lt;label&gt; tag that references the field, which must have an ID.
 * LabeledField is an Editor so that in many cases, applications can avoid keeping two component references
 * for each field.
 */
abstract class LabeledField[T](val labelContent: Component, val field: Field[T]) extends Editor[T] {

  val label = Label(labelContent, field)

  def value = field.value

  def fields = field.fields

  def modelGet() = field.modelGet

  def modelSet(value: T) { field.modelSet(value) }
}

class LeftLabeledField[T](labelContent: Component, field: Field[T]) extends LabeledField[T](labelContent, field) with Log {

  def render() = FormStyle.renderLeftLabeledField(this)
}

object LeftLabeledField {

  def apply[T](labelContent: Component, field: Field[T], labeledFieldBehaviors: Behavior*) = new LeftLabeledField(labelContent, field) {
    override def behaviors = labeledFieldBehaviors
  }
}

class RightLabeledField[T](labelContent: Component, field: Field[T]) extends LabeledField[T](labelContent, field) with Log {

  def render() = FormStyle.renderRightLabeledField(this)
}

object RightLabeledField {

  def apply[T](labelContent: Component, field: Field[T], labeledFieldBehaviors: Behavior*) = new RightLabeledField(labelContent, field) {
    override def behaviors = labeledFieldBehaviors
  }
}

