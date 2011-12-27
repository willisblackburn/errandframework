/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package ajax

import org.errandframework.components._
import forms.{Choice, TextField}
import org.errandframework.http._
import java.util.UUID
import pages.Page

/**
 * AjaxTextField is an input control that supports a type-ahead menu that the control retrieves from an
 * AJAX handler.
 */
abstract class AjaxTextField[T](page: Page, parameter: Parameter[T]) extends TextField[T](parameter) with EmbeddedController {

  import AjaxTextField.valueParameter

  override def render() = {
    AjaxSupport.add
    Page.addHeadContent(AjaxTextField.script)
    AjaxTextFieldStyle.renderAjaxTextField(this)
  }

  val menuId = UUID.randomUUID.toString

  def onKeyDownJavaScript = "return AjaxTextField_handleKeyDown(event, $('" + menuId + "'), this, '" + url + "')"

  def onBlurJavaScript = "AjaxTextField_hideMenu($('" + menuId + "'))"

  def controller = new Controller with ParameterProcessor {
    def parameters = Seq(valueParameter)
    def onValid(request: Request) = {
      CharacterBufferResponse("application/json",
        choices(valueParameter.get).map(choice => "'" + parameter.encodeAsString(choice.value) + "'").mkString("{ choices: [", ", ", "] }"))
    }
  }

  def choices(value: String): Iterable[Choice[T]]
}

object AjaxTextField {

  def apply[T](page: Page, name: String, fieldChoices: String => Iterable[Choice[T]], fieldModelGet: => T, fieldModelSet: T => Unit,
               fieldBehaviors: Behavior*)(implicit codec: Codec[T]) =
    withParameter(page, Parameter[T](name), fieldChoices, fieldModelGet, fieldModelSet, fieldBehaviors: _*)

  def withParameter[T](page: Page, parameter: Parameter[T], fieldChoices: String => Iterable[Choice[T]], fieldModelGet: => T, fieldModelSet: T => Unit,
                       fieldBehaviors: Behavior*)(implicit codec: Codec[T]) =

    new AjaxTextField[T](page, parameter) {

      def modelGet() = fieldModelGet

      def modelSet(value: T) {
        fieldModelSet(value)
      }

      def choices(value: String) = fieldChoices(value)

      override def behaviors = fieldBehaviors
    }

  val valueParameter = Parameter[String]("value")

  val script = Script.fromUrl(ResourceServerControllerProvider.urlForResource(Path(classOf[AjaxTextField[_]].getPackage) / "AjaxTextField.js"))
}