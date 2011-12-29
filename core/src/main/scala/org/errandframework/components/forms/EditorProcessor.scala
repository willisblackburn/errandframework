/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package forms

import javax.servlet.http.HttpServletResponse._
import org.errandframework.http._

/**
 * EditorProcessor is like ParameterProcessor but adds additional logic for processing editors.
 * EditorProcessor uses ParameterProcessor to decode each parameter, then either (on success) updates each
 * editor's backing model or (on failure) records error messages.
 */
trait EditorProcessor extends Controller {

  private val processor = new ParameterProcessor {

    def parameters = for (editor <- editors; field <- editor.fields) yield field.parameter

    def onValid(request: Request) = {
      for (editor <- editors)
        editor.updateModel
      if (EditorProcessor.this.validate)
        EditorProcessor.this.onValid(request)
      else
        EditorProcessor.this.onError(request)
    }

    override def onError(request: Request) = {
      for (editor <- editors)
        editor.value.invalid.foreach(Message.error(_, Some(editor)))
      EditorProcessor.this.onError(request)
    }
  }

  def respond(request: Request) = processor.respond(request)

  def editors: Seq[Editor[_]]

  def validate(): Boolean = true

  def onValid(request: Request): Response

  def onError(request: Request): Response = StatusResponse(SC_BAD_REQUEST)
}