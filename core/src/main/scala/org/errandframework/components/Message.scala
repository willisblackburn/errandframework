/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import collection.mutable.ListBuffer
import org.errandframework.http.RequestVar

trait MessageType {
  val priority: Int
}

object MessageType {
  val DEBUG = new MessageType { val priority = 0 }
  val INFO = new MessageType { val priority = 1 }
  val WARNING = new MessageType { val priority = 2 }
  val ERROR = new MessageType { val priority = 3 }
}

import MessageType._

case class Message(messageType: MessageType, message: String, marker: Option[Any])

/**
 * Messages is a parking place for messages that need to be displayed to the user.
 */
object Message {

  // TODO, internationalization/localization.
  // TODO, add information concerning:  message level, field/component, resource bundle.

  val _messages = RequestVar(new ListBuffer[Message])

  def messages: Seq[Message] = _messages.value

  def messageCount = _messages.value.length

  def debug(message: String, marker: Option[Any] = None) = _messages.value += Message(DEBUG, message, marker)

  def info(message: String, marker: Option[Any] = None) = _messages.value += Message(INFO, message, marker)

  def warning(message: String, marker: Option[Any] = None) = _messages.value += Message(WARNING, message, marker)

  def error(message: String, marker: Option[Any] = None) = _messages.value += Message(ERROR, message, marker)

  def add(messages: Message*) = _messages.value ++= messages

  def clear() = _messages.value.clear
}
