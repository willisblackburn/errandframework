/*
* Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
*/

package org.errandframework.components

import org.errandframework.http._

/**
 * Filter that supports temporarily stashing messages when the server sends a redirect and
 * reloading them after receiving the next request.
 * MessageStasher avoids creating a session unnecessarily by only saving messages in the session if there are any
 * to save and only if the server is sending a redirect, and by only attempting to restore messages if the session
 * actually exists.
 */
object MessageStasherFilter extends RequestFilter {

  private val stash = SessionVar[Seq[Message]](Seq.empty)

  def filter(request: Request)(respond: (Request) => Response) = {
    if (RequestContext.hasSession) {
      // Restore any previously-stashed messages, then clear the stash.
      Message.add(stash.value: _*)
      stash.value = Seq.empty
    }
    respond(request) match {
      case response: RedirectResponse =>
        // Stash the messages. Since this is a redirect response, the original
        // messages (which are kept in the request) will go away soon.
        // Ignore IllegalStateException, which may occur if the session was invalidated during the processing of
        // the request.
        try {
          if (Message.messageCount > 0)
            stash.value = Message.messages
        } catch {
          case e: IllegalStateException => ()
        }
        response
      case response =>
        response
    }
  }
}
