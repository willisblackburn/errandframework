/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import xml.{Elem, Node, Text, NodeSeq}

/**
 * Component is a renderable XHTML entity.
 */
trait Component extends Serializable {

  /**
   * Renders the component.
   */
  def render(): NodeSeq

  def renderIfVisible(xhtml: => Seq[Node]): NodeSeq = visible match {
    case true => xhtml
    case _ => Seq.empty
  }

  def visible: Boolean = true

  def enabled: Boolean = true

  /**
   * Retrieve the component's ID, if IdBehavior is present.
   */
  def id: Option[String] = behaviors collectFirst { case behavior: IdBehavior => behavior.id }

  def transform(elem: Elem) = Behavior.transform(elem, behaviors: _*)

  def behaviors: Seq[Behavior] = Seq.empty
}

object Component {

  def apply(xhtml: => NodeSeq, componentBehaviors: Behavior*) = new Component {
    def render() = xhtml.flatMap {
      case elem: Elem => transform(elem)
      case other => other
    }
    override def behaviors = componentBehaviors
  }

  // Can't accept NodeSeq for these two functions because the implicit conversion to NodeSeq will
  // prevent the pass-by-name from working properly.

  implicit def fromFunction(f: () => Seq[Node]) = new Component {
    def render() = f()
  }

  implicit def fromXhtml(xhtml: => Seq[Node]) = new Component {
    def render() = xhtml
  }

  implicit def fromString(s: => String) = new Component {
    def render() = Text(s)
  }

  val empty = new Component {
    def render() = Seq.empty
    override def visible = false
  }
}
