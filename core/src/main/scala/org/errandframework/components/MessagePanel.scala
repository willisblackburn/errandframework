/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import MessageType._

/**
 * Methods for displaying the message panel.
 */
class MessagePanel(messageTypes: Set[MessageType] = Set(INFO, WARNING, ERROR), marker: Option[Any] = None) extends Component {

  def render() = {
    val displayMessages = Message.messages.filter(m => messageTypes.contains(m.messageType) && (marker == None || marker == m.marker))
    if (displayMessages.length > 0) {
      <div class="errand-MessagePanel">{
          displayMessages.flatMap(m => <div class={cssClass(m.messageType)}>{m.message}</div>)
        }</div>
    } else
      Seq.empty
  }

  def cssClass(messageType: MessageType): String = messageType match {
    case DEBUG => "debug"
    case INFO => "info"
    case WARNING => "warning"
    case ERROR => "error"
  }
}
