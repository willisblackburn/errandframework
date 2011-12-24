/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import pages.Page
import xml.{Attribute, Null, Elem, Text}
import java.util.UUID

/**
 * Behavior that runs some JavaScript when an event (or one of a set of events) occurs.
 * A nonzero delay value will cause the framework to wait that many milliseconds before executing the
 * event JavaScript.  Any new events will reset the delay.
 */
abstract class EventBehavior(eventAttributes: Set[String]) extends Behavior {

  def this(eventAttribute: String) = this(Set(eventAttribute))

  lazy val timerId = UUID.randomUUID.toString

  def transform(component: Component, elem: Elem) = {
    MooToolsSupport.add
    (elem /: eventAttributes)((e, key) => e % Attribute(key, Text(javaScriptWithDelay), Null))
  }

  def javaScriptWithDelay = "var _errand_this = this; " + (delay match {
    case 0 =>
      javaScript
    case _ =>
      require(delay >= 0)
      "clearTimeout(this.retrieve('_errand_timer_" + timerId + "')); this.store('_errand_timer_\" + timerId + \"', setTimeout(function() { " + javaScript + " }, " + delay + "));"
  })


  def javaScript: String

  def delay: Int = 0
}

object EventBehavior {

  def apply(eventAttributes: Set[String], behaviorJavaScript: String) = new EventBehavior(eventAttributes) {
    def javaScript = behaviorJavaScript
  }

  def apply(eventAttribute: String, behaviorJavaScript: String): EventBehavior = apply(Set(eventAttribute), behaviorJavaScript)

  def apply(eventAttributes: Set[String], behaviorJavaScript: String, behaviorDelay: Int) = new EventBehavior(eventAttributes) {
    def javaScript = behaviorJavaScript
    override def delay = behaviorDelay
  }

  def apply(eventAttribute: String, behaviorJavaScript: String, behaviorDelay: Int): EventBehavior = apply(Set(eventAttribute), behaviorJavaScript, behaviorDelay)
}
