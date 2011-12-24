/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package ajax

import xml.Elem
import org.errandframework.http._
import javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST
import forms.{EditorProcessor, Field, Editor}
import org.errandframework.util.Log

/**
 * Adds the ability to handle a JavaScript event on the server and update a section of the page.
 */
abstract class AjaxFormActionBehavior(eventAttributes: Set[String]) extends EventBehavior(eventAttributes) with EmbeddedController {

  import AjaxFormActionBehavior.log

  def this(eventAttribute: String) = this(Set(eventAttribute))

  def controller = new Controller with EditorProcessor {

    def editors = AjaxFormActionBehavior.this.editors

    override def validate() = AjaxFormActionBehavior.this.validate

    def onValid(request: Request) = AjaxFormActionBehavior.this.onEvent

    override def onError(request: Request) = AjaxFormActionBehavior.this.onError
  }

  def javaScript = {
    def buildReference(field: Field[_]): Option[String] = field.id match {
      case Some(id) => Some("$('" + id + "')")
      case _ =>
        log.warn("Field used in AjaxActionBehavior does not have an ID: " + field)
        None
    }
    val references = for (editor <- editors; field <- editor.fields; reference <- buildReference(field)) yield reference
    "return ajaxAction('" + url + "', " + references.mkString("[", ",", "]") + ")"
  }

  override def transform(component: Component, elem: Elem) = {
    AjaxSupport.add
    super.transform(component, elem)
  }

  def editors: Seq[Editor[_]] = Seq.empty

  def validate(): Boolean = true

  def onEvent(): Response

  def onError(): Response
}

object AjaxFormActionBehavior extends Log {

  def apply(eventAttributes: Set[String])(behaviorOnEvent: => Response) = new AjaxFormActionBehavior(eventAttributes) {
    def onEvent() = behaviorOnEvent
    def onError() = StatusResponse(SC_BAD_REQUEST)
  }

  def apply(eventAttribute: String)(behaviorOnEvent: => Response): AjaxFormActionBehavior = apply(Set(eventAttribute))(behaviorOnEvent)

  def apply(eventAttributes: Set[String], behaviorDelay: Int, behaviorEditors: => Seq[Editor[_]], behaviorValidate: => Boolean)(behaviorOnEvent: => Response) = new AjaxFormActionBehavior(eventAttributes) {
    override def editors = behaviorEditors
    override def delay = behaviorDelay
    def onEvent() = behaviorOnEvent
    def onError() = StatusResponse(SC_BAD_REQUEST)
  }

  def apply(eventAttribute: String, behaviorDelay: Int, behaviorEditors: => Seq[Editor[_]], behaviorValidate: => Boolean)(behaviorOnEvent: => Response): AjaxFormActionBehavior = apply(Set(eventAttribute), behaviorDelay, behaviorEditors, behaviorValidate)(behaviorOnEvent)
}
