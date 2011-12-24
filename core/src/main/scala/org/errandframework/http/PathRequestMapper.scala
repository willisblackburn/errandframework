/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * Mapper that translates locations to specific controllers.
 */
class PathRequestMapper(mapping: PartialFunction[Path, Controller]) extends RequestMapper {

  def resolve(request: Request) = mapping.lift(request.path).map(controller => Seq((controller, 1))).getOrElse(Seq.empty)
}
