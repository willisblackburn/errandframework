/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package forms

import xml.{Text, NodeSeq}
import util.DynamicVariable
import org.errandframework.util.XmlHelpers._
import org.errandframework.http.RequestContext

/**
 * Methods for rendering forms.
 */
trait FormStyle {

  def renderForm(component: Form): NodeSeq

  def renderInputField[T](component: InputField[T]): NodeSeq

  def renderFileField(component: FileField): NodeSeq

  def renderCheckboxField(component: CheckboxField): NodeSeq

  def renderRadioField[T](component: RadioField[T]): NodeSeq

  def renderTextareaField[T](component: TextareaField[T]): NodeSeq

  def renderSelectField[T](component: SelectField[T]): NodeSeq

  def renderLabel(component: Label): NodeSeq

  def renderLeftLabeledField[T](component: LabeledField[T]): NodeSeq

  def renderRightLabeledField[T](component: LabeledField[T]): NodeSeq

  def renderButton(component: Button): NodeSeq
}

object FormStyle extends DynamicVariable[FormStyle](DefaultFormStyle) with FormStyle {

  def renderForm(component: Form) = value.renderForm(component)

  def renderInputField[T](component: InputField[T]) = value.renderInputField[T](component)

  def renderFileField(component: FileField) = value.renderFileField(component)

  def renderCheckboxField(component: CheckboxField) = value.renderCheckboxField(component)

  def renderRadioField[T](component: RadioField[T]) = value.renderRadioField(component)

  def renderTextareaField[T](component: TextareaField[T]) = value.renderTextareaField(component)

  def renderSelectField[T](component: SelectField[T]) = value.renderSelectField(component)

  def renderLabel(component: Label): NodeSeq = value.renderLabel(component)

  def renderLeftLabeledField[T](component: LabeledField[T]): NodeSeq = value.renderLeftLabeledField(component)

  def renderRightLabeledField[T](component: LabeledField[T]): NodeSeq = value.renderRightLabeledField(component)

  def renderButton(component: Button) = value.renderButton(component)

  val checkedValue = Some(Text("checked"))
  val selectedValue = Some(Text("selected"))
  val disabledValue = Some(Text("disabled"))
  val multipleValue = Some(Text("multiple"))

  def disabled(component: Component) = if (component.enabled) None else disabledValue
}

// TODO, numerous Law of Demeter violations in this class.

object DefaultFormStyle extends FormStyle {

  import FormStyle.disabled

  def renderForm(component: Form) = component.renderIfVisible {
    component.transform(<form method={component.method.toString} enctype={component.encodingType.toString} action={RequestContext.encodeURL(component.url)} class="errand-Form">{component.content.render}</form>)
  }

  def renderInputField[T](component: InputField[T]): NodeSeq = component.renderIfVisible {
    component.transform(<input type={component.inputType} name={component.name} value={component.encode} disabled={disabled(component)}
        class="errand-InputField"/>)
  }

  def renderFileField(component: FileField) = component.renderIfVisible {
    val multiple = if (component.multiple) FormStyle.multipleValue else None
    component.transform(<input type="file" name={component.name} multiple={multiple} disabled={disabled(component)} class="errand-FileField"/>)
  }

  def renderCheckboxField(component: CheckboxField) = component.renderIfVisible {
    val checked = if (component.getOrElse(component.modelGet)) FormStyle.checkedValue else None
    component.transform(<input type="checkbox" name={component.name} value="true" checked={checked} disabled={disabled(component)} class="errand-CheckboxField"/>)
  }

  def renderRadioField[T](component: RadioField[T]) = component.renderIfVisible {
    val checked = if (component.getOrElse(component.modelGet) == component.radioValue) FormStyle.checkedValue else None
    component.transform(<input type="radio" name={component.name} value={component.encode} checked={checked} disabled={disabled(component)} class="errand-RadioField"/>)
  }

  def renderTextareaField[T](component: TextareaField[T]) = component.renderIfVisible {
    component.transform(<textarea name={component.name} disabled={disabled(component)} class="errand-TextareaField">{component.encode}</textarea>)
  }

  def renderSelectField[T](component: SelectField[T]) = component.renderIfVisible {
    val parameterValue = component.getOrElse(component.modelGet)
    component.transform(<select name={component.name} disabled={disabled(component)} class="errand-SelectField">{
      for (option <- component.options) yield {
        val selected = if (parameterValue == option.value) FormStyle.selectedValue else None
        <option value={component.parameter.encodeAsString(option.value)} selected={selected}>{option.description.render}</option>
      }
    }</select>)
  }

  def renderLabel(component: Label) = {
    component.transform(<label for={asText(component.field.id)} class="errand-Label">{component.content.render}</label>)
  }

  def renderLeftLabeledField[T](component: LabeledField[T]) = component.renderIfVisible {
    component.transform(<div class="errand-LeftLabeledField">
      {component.label.render}<span class="errand-LeftLabeledField-field">{component.field.render}</span>
    </div>)
  }

  def renderRightLabeledField[T](component: LabeledField[T]) = component.renderIfVisible {
    component.transform(<div class="errand-RightLabeledField">
      <span class="errand-RightLabeledField-field">{component.field.render}</span>{component.label.render}
    </div>)
  }

  def renderButton(component: Button) = component.renderIfVisible {
    component.transform(<button type={component.buttonType} disabled={disabled(component)} class="errand-Button">{component.content.render}</button>)
  }
}
