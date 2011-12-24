/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package forms

import org.errandframework.http.Path._
import java.util.UUID
import org.errandframework.http._
import org.errandframework.components.pages.Page

/**
 * A control that lets a user enter several different terms and creates a bubble from each.
 */
abstract class BubbleField[T](override val parameter: MultiParameter[T]) extends Field[Seq[T]](parameter) {

  def render() = {
    Page.addHeadContent(BubbleField.script)
    Page.addHeadContent(onDomReadyScript)
    BubbleFieldStyle.renderBubbleField(this)
  }

  /**
   * Encodes each of the BubbleField values as a string, using the parameter codec.
   */
  def encode(): Seq[String] = parameter.getOrElse(modelGet).map(parameter.encodeAsString)

  val bubbleListId = UUID.randomUUID.toString

  def onKeyDownJavaScript = "return BubbleField_handleKeyDown(event, $('" + bubbleListId + "'), this, '" + parameter.name + "')"

  // Register an event handler to transform any existing BubbleField lists after the DOM is loaded.

  def onDomReadyScript = Script("""
window.addEvent('domready', function() {
    $('""" + bubbleListId + """').getElements('li').each(function(item) {
        BubbleField_transformItem(item, '""" + parameter.name + """');
    });
});
""")
}

object BubbleField {

  def apply[T](name: String, fieldModelGet: => Seq[T], fieldModelSet: Seq[T] => Unit,
               fieldBehaviors: Behavior*)(implicit codec: Codec[T]) =
    withParameter(MultiParameter[T](name), fieldModelGet, fieldModelSet, fieldBehaviors: _*)

  def withParameter[T](parameter: MultiParameter[T], fieldModelGet: => Seq[T], fieldModelSet: Seq[T] => Unit,
                       fieldBehaviors: Behavior*)(implicit codec: Codec[T]) =

    new BubbleField(parameter) {

      def modelGet() = fieldModelGet

      def modelSet(value: Seq[T]) {
        fieldModelSet(value)
      }

      override def behaviors = fieldBehaviors
    }

  val script = Script.fromUrl(ResourceServerControllerProvider.urlForResource(Path(classOf[BubbleField[_]].getPackage) / "BubbleField.js"))
}