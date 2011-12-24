/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST

/**
 * <p>ParameterProcessor is an add-in for Controller that implements some boilerplate features common
 * to controllers that accept parameters.</p>
 * <p>Subclasses of Controller with ParameterProcessor do not implement the respond method.  Instead they
 * implement the methods validate, onError and onValid.
 * <ul>
 *   <li>validate checks that all of the parameters, taken together, are valid.  Individual parameters
 *   have their own validate methods;  this method performs any validations that require more than
 *   one parameter.</li>
 *   <li>onError handles any errors.  By default this method returns a status response.</li>
 *   <li>onValid generate the response after all of the validation checks have been performed.</li>
 * </ul>
 * <p>ParameterProcessor implementations must also provide the parameters set, which identifies the
 * parameters to process.  ParameterProcessor invokes the decodeAndSet method on each parameter, so that
 * in the onValid method, the subclass may access the value for each parameter using the get method.
 */
trait ParameterProcessor extends Controller {

  def respond(request: Request) = {
    if (decodeAndSetParameters)
      onValid(request)
    else
      onError(request)
  }

  private def decodeAndSetParameters() = {
    (true /: parameters)((ok, parameter) => parameter.decodeAndSet.isValid && ok)
  }

  def parameters: Seq[RequestParameter[_]]

  def validate(): Boolean = true

  def onValid(request: Request): Response

  def onError(request: Request): Response = StatusResponse(SC_BAD_REQUEST)
}