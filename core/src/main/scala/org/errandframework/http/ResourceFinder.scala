/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

import org.jclouds.io.Payload

/**
 * Interface for classes that can locate static resources based on a path.
 * This interface is designed to merge the concepts of loading a static resource from the classpath, the
 * web application, and jclouds.
 */
trait ResourceFinder {

  /**
   * Accesses the resource and returns it as a jclouds Payload object.
   * @param path the path under which the resource is stored.
   * @throws ResourceNotFoundException if a resource with the given path does not exist.
   */
  def get(path: Path): Payload
}