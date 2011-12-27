/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import collection.mutable

/**
 * DynamicController maps to other controllers that are dynamically registered and kept in the session.
 * Each controller is identified by an integer ID.
 * The register method returns the ID.  The caller must built id into a URL.
 */
class DynamicController extends Controller with ParameterProcessor {

  import DynamicController.controllerIdParameter

  private val _nextId = SessionVar[Int](0)
  private def nextId = {
    val n = _nextId.value
    _nextId.value = n + 1
    n
  }

  private val _controllers = SessionVar[mutable.Map[Int, Controller]](new mutable.HashMap[Int, Controller])
  private def controllers = _controllers.value

  /**
   * Registers a new controller and returns the ID.
   */
  def register(controller: Controller): Int = {
    require(controller != null)
    RequestContext.session.synchronized {
      val id = nextId
      controllers(id) = controller
      id
    }
  }

  def parameters = Seq(controllerIdParameter)

  def onValid(request: Request) = controllers(controllerIdParameter.get).respond(request)
}

object DynamicController {

  val controllerIdParameter = Parameter[Int]("_errand_controller_id")
}