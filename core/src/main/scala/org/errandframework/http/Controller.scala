/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * A controller handles a web request.
 */
trait Controller {

  def respond(request: Request): Response
}

object Controller {

  implicit def fromFunction(f: Request => Response) = new Controller {
    def respond(request: Request) = { f(request) }
  }

  implicit def fromResponse(f: => Response) = new Controller {
    def respond(request: Request) = { f }
  }
}