/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package forms

import pages.{Page, PageResponse}
import org.errandframework.http._
import org.errandframework.http.BuiltinCodecs.IntCodec

abstract class Button(val buttonType: String, val content: Component) extends Component {

  def render() = FormStyle.renderButton(this)
}

abstract class SubmitButton(content: Component) extends Button("submit", content)

object SubmitButton {

  def apply(content: Component, buttonBehaviors: Behavior*) = new SubmitButton(content) {
    override def behaviors = buttonBehaviors
  }
}

abstract class ResetButton(content: Component) extends Button("reset", content)

object ResetButton {

  def apply(content: Component, buttonBehaviors: Behavior*) = new ResetButton(content) {
    override def behaviors = buttonBehaviors
  }
}

abstract class PushButton(content: Component) extends Button("button", content)

object PushButton {

  def apply(content: Component, buttonBehaviors: Behavior*) = new PushButton(content) {
    override def behaviors = buttonBehaviors
  }
}

abstract class ActionButton(content: Component) extends SubmitButton(content) with EmbeddedController {

  def controller = new Controller with EditorProcessor {

    def editors = ActionButton.this.editors

    override def validate() = ActionButton.this.validate

    def onValid(request: Request) = ActionButton.this.onSubmit

    override def onError(request: Request) = ActionButton.this.onError
  }

  override def behaviors = Seq(Behavior.name(DynamicController.controllerIdParameter.name), Behavior.value[Int](controllerId))

  def editors: Seq[Editor[_]]

  def validate(): Boolean = true

  def onSubmit(): Response

  def onError(): Response
}

object ActionButton {

  def apply(page: Page, content: String, buttonEditors: => Seq[Editor[_]], buttonValidate: => Boolean, buttonBehaviors: Behavior*)
           (buttonOnSubmit: => Response) = new ActionButton(content) {

    override def behaviors = super.behaviors ++ buttonBehaviors
    def editors = buttonEditors
    override def validate() = buttonValidate
    def onSubmit() = buttonOnSubmit
    def onError() = PageResponse(page)
  }
}