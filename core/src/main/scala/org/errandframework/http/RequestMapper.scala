/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * RequestMapper translates between a request URL and a set of Controller instances.
 * Normally there is just one Controller per URL, but there can be several, each with a different score.
 * The framework will choose the controller with the highest score.
 * A common use of score is to represent the number of matched path components:  for example, if the application
 * maps any URL starting with /a to controller X and any URL starting with /a/b to controller Y, then the
 * application could define the /a match to have score=1 and the /a/b match to have score=2, insuring that any
 * URL starting with /a/b maps to controller Y and not controller X.
 */
trait RequestMapper {

  def resolve(request: Request): Seq[(Controller, Int)]
}
