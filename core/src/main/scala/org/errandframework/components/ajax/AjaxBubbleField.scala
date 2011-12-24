/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package ajax

import java.util.UUID
import org.errandframework.http._
import pages.Page
import forms.{Choice, BubbleField}

/**
 * AjaxBubbleField combines BubbleField with AjaxTextField.  It features a bubble list like BubbleField, and
 * also displays a type-ahead menu.
 * AjaxBubbleField uses functionality from BubbleField via inheritance and also borrows some code from
 * AjaxTextField.
 */
abstract class AjaxBubbleField[T](page: Page, parameter: MultiParameter[T]) extends BubbleField[T](parameter) with EmbeddedController {

  import AjaxTextField.valueParameter

  override def render() = {
    Page.addHeadContent(BubbleField.script)
    Page.addHeadContent(AjaxTextField.script)
    Page.addHeadContent(onDomReadyScript)
    AjaxBubbleFieldStyle.renderAjaxBubbleField(this)
  }

  val menuId = UUID.randomUUID.toString

  override def onKeyDownJavaScript = "return AjaxTextField_handleKeyDown(event, $('" + menuId + "'), this, '" + url + "') && " +
    "BubbleField_handleKeyDown(event, $('" + bubbleListId + "'), this, '" + parameter.name + "')"

  def onBlurJavaScript = "AjaxTextField_hideMenu($('" + menuId + "'))"

  def controller = new Controller with ParameterProcessor {
    def parameters = Seq(valueParameter)
    def onValid(request: Request) = {
      CharacterBufferResponse(MediaType("application", "json"),
        choices(valueParameter.get).map(choice => "'" + parameter.encodeAsString(choice.value) + "'").mkString("{ choices: [", ", ", "] }"))
    }
  }

  def choices(value: String): Iterable[Choice[T]]
}

object AjaxBubbleField {

  def apply[T](page: Page, name: String, fieldChoices: String => Iterable[Choice[T]], fieldModelGet: => Seq[T], fieldModelSet: Seq[T] => Unit,
               fieldBehaviors: Behavior*)(implicit codec: Codec[T]) =
    withParameter(page, MultiParameter[T](name), fieldChoices, fieldModelGet, fieldModelSet, fieldBehaviors: _*)

  def withParameter[T](page: Page, parameter: MultiParameter[T], fieldChoices: String => Iterable[Choice[T]], fieldModelGet: => Seq[T], fieldModelSet: Seq[T] => Unit,
                       fieldBehaviors: Behavior*)(implicit codec: Codec[T]) =

    new AjaxBubbleField[T](page, parameter) {

      def modelGet() = fieldModelGet

      def modelSet(value: Seq[T]) {
        fieldModelSet(value)
      }

      def choices(value: String) = fieldChoices(value)

      override def behaviors = fieldBehaviors
    }
}