/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import org.errandframework.http._

/**
 * EmbeddedController adds a controller to either a Component or a Behavior and registers it with the dynamic
 * controller.
 */
trait EmbeddedController {

  def controller: Controller

  /**
   * The dynamic controller URL.
   */
  def url = DynamicControllerProvider.urlForControllerId(controllerId)

  def localUrl = DynamicControllerProvider.localUrlForControllerId(controllerId)

  protected lazy val controllerId = DynamicControllerProvider.registerController(controller)
}