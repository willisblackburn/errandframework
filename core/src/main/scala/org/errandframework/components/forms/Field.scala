/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package forms

import org.errandframework.http._
import xml.Node
import javax.servlet.http.Part

/**
 * Field is a particular type of Editor that maps to a single RequestParameter and usually a single HTML
 * control.  The RequestParameter takes care of decoding the value from the request, while the field renders
 * the HTML control and connects the value to the backing model.
 */
abstract class Field[T](val parameter: RequestParameter[T]) extends Editor[T] {

  def fields = Seq(this)

  def name = parameter.name

  def value = parameter.value
}

abstract class InputField[T](override val parameter: Parameter[T], val inputType: String) extends Field[T](parameter) {

  def render() = FormStyle.renderInputField(this)

  // TODO, move these to Parameter.  Maybe have a behavior that renders JavaScript to validate them.

//  /**
//   * Minimum length for the field.
//   * Set to &gt;0 to make the field required.
//   * Enforced at submit time.
//   */
//  def minLength: Option[Int] = None
//
//  /**
//   * Maximum length for the field.
//   * Expressed in HTML and also enforced at submit time.
//   */
//  def maxLength: Option[Int] = None
//
//  override def decode() = {
//    super.decode.valid flatMap {
//      v => val s = v.toString
//      // Can't really validate "length" for anything but strings.
//      val minFilter = minLength.filter(s.length < _)
//      val maxFilter = maxLength.filter(s.length > _)
//      (minFilter, maxFilter) match {
//        case (Some(min), _) => Invalid(parameter.formatErrorMessage("short")(name, min))
//        case (_, Some(max)) => Invalid(parameter.formatErrorMessage("long")(name, max))
//        case _ => Valid(v)
//      }
//    }
//  }

  def encode(): String = parameter.fold(parameter.encodeAsString, _ => parameter.raw.getOrElse(""), parameter.encodeAsString(modelGet))
}

abstract class TextField[T](parameter: Parameter[T]) extends InputField[T](parameter, "text")

object TextField {

  // Note: Don't create overloaded apply methods, as this causes Scala's type inference to fail and forces
  // developers to identify the type of T in the fieldModelSet method.

  def apply[T](name: String, fieldModelGet: => T, fieldModelSet: T => Unit,
               fieldBehaviors: Behavior*)(implicit codec: Codec[T]) =
    withParameter(Parameter[T](name), fieldModelGet, fieldModelSet, fieldBehaviors: _*)

  def withParameter[T](parameter: Parameter[T], fieldModelGet: => T, fieldModelSet: T => Unit,
               fieldBehaviors: Behavior*)(implicit codec: Codec[T]) =

    new TextField[T](parameter) {

      def modelGet() = fieldModelGet

      def modelSet(value: T) {
        fieldModelSet(value)
      }

//      override def minLength = fieldMinLength
//
//      override def maxLength = fieldMaxLength

      override def behaviors = fieldBehaviors
    }

}

abstract class PasswordField[T](parameter: Parameter[T]) extends InputField[T](parameter, "password")

object PasswordField {

  def apply[T](name: String, fieldModelGet: => T, fieldModelSet: T => Unit,
               fieldBehaviors: Behavior*)(implicit codec: Codec[T]) =
    new PasswordField[T](Parameter(name)) {

      def modelGet() = fieldModelGet

      def modelSet(value: T) {
        fieldModelSet(value)
      }
//      override def minLength = fieldMinLength
//
//      override def maxLength = fieldMaxLength

      override def behaviors = fieldBehaviors
    }
}

abstract class HiddenField[T](parameter: Parameter[T]) extends InputField[T](parameter, "hidden")

object HiddenField {

  def apply[T](name: String, fieldModelGet: => T, fieldModelSet: T => Unit)(implicit codec: Codec[T]) =
    new HiddenField[T](Parameter(name)) {

      def modelGet() = fieldModelGet

      def modelSet(value: T) {
        fieldModelSet(value)
      }
    }
}

class FileField(override val parameter: FileParameter) extends Field[Seq[Part]](parameter) {

  def render() = FormStyle.renderFileField(this)

  def modelGet() = throw new UnsupportedOperationException("FieldField does not support a model")

  def modelSet(value: Seq[Part]) {}

//  override def formMethod = Some(POST)

//  // TODO, use MediaType?
//  override def formEncodingType = Some("multipart/form-data")

  def multiple = false
}

object FileField {

  def apply(name: String, fieldMultiple: Boolean, fieldBehaviors: Behavior*) = new FileField(FileParameter(name)) {
    override def multiple = fieldMultiple
    override def behaviors = fieldBehaviors
  }
}

abstract class CheckboxField(override val parameter: BooleanParameter) extends Field[Boolean](parameter) {
  def render() = FormStyle.renderCheckboxField(this)
}

object CheckboxField {

  def apply(name: String, fieldModelGet: => Boolean, fieldModelSet: Boolean => Unit, fieldBehaviors: Behavior*) =
    new CheckboxField(BooleanParameter(name)) {

      def modelGet() = fieldModelGet

      def modelSet(value: Boolean) {
        fieldModelSet(value)
      }

      override def behaviors = fieldBehaviors
    }
}

abstract class RadioField[T](override val parameter: Parameter[T], val radioValue: T) extends Field[T](parameter) {
  def render() = FormStyle.renderRadioField(this)

  def encode(): String = parameter.encodeAsString(radioValue)
}

object RadioField {

  def apply[T](name: String, radioValue: T, fieldModelGet: => T, fieldModelSet: T => Unit, fieldBehaviors: Behavior*)(implicit codec: Codec[T]) =
    new RadioField(Parameter[T](name), radioValue) {

      def modelGet() = fieldModelGet

      def modelSet(value: T) {
        fieldModelSet(value)
      }

      override def behaviors = fieldBehaviors
    }
}

abstract class TextareaField[T](override val parameter: Parameter[T]) extends Field[T](parameter) {
  def render() = FormStyle.renderTextareaField(this)

  def encode(): String = parameter.fold(parameter.encodeAsString, _ => "", parameter.encodeAsString(modelGet))
}

object TextareaField {

  def apply[T](name: String, fieldModelGet: => T, fieldModelSet: T => Unit, fieldBehaviors: Behavior*)(implicit codec: Codec[T]) =
    new TextareaField[T](Parameter(name)) {

      def modelGet() = fieldModelGet

      def modelSet(value: T) {
        fieldModelSet(value)
      }

      override def behaviors = fieldBehaviors
    }
}

abstract class SelectField[T](override val parameter: Parameter[T]) extends Field[T](parameter) {

  def options: Iterable[Choice[T]]

  def render() = FormStyle.renderSelectField(this)
}

object SelectField {

  def apply[T](name: String, fieldModelGet: => T, fieldModelSet: T => Unit, fieldOptions: Iterable[Choice[T]], fieldBehaviors: Behavior*)(implicit codec: Codec[T]) =
    new SelectField[T](Parameter(name)) {

      def modelGet() = fieldModelGet

      def modelSet(value: T) {
        fieldModelSet(value)
      }

      def options = fieldOptions

      override def behaviors = fieldBehaviors
    }
}

case class Choice[T](value: T, description: Component)

object Choice {

  // Define some implicit conversions for Choice.

  implicit def fromStringKey[T](choice: (T, String)) = Choice[T](choice._1, choice._2)

  implicit def fromXhtmlKey[T](choice: (T, Seq[Node])) = Choice[T](choice._1, choice._2)

  implicit def fromComponentKey[T](choice: (T, Component)) = Choice[T](choice._1, choice._2)

  implicit def fromAny[T](value: T) = Choice[T](value, value.toString)
}
