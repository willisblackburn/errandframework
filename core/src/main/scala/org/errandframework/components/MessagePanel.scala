/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

// TODO, does this need a style class?  Seems like it should.
// TODO, this uses 960GS classes, but we don't assume 960gs in Errand.

import MessageType._

/**
 * Methods for displaying the message panel.
 */
class MessagePanel(messageTypes: Set[MessageType] = Set(INFO, WARNING, ERROR), marker: Option[Any] = None) extends Component {

  def render() = {
    val displayMessages = Message.messages.filter(m => messageTypes.contains(m.messageType) && (marker == None || marker == m.marker))
    if (displayMessages.length > 0) {
      <div class="messageContainer container_12">
        <div class="messagePanel grid_12">{
            displayMessages.flatMap(m => <div class={cssClass(m.messageType)}>{m.message}</div>)
        }</div>
      </div>
    } else
      Seq.empty
  }

  def cssClass(messageType: MessageType): String = messageType match {
    case DEBUG => "success"
    case INFO => "success"
    case WARNING => "notice"
    case ERROR => "error"
  }
}
