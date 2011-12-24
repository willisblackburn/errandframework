/*
* Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
*/

package org.errandframework.http

import java.io.InputStream
import java.net.URL

/**
 * Helper methods to make certain class path operations more Scala-friendly.
 */
object ClassPathHelpers {

  def getResource(path: Path): Option[URL] = Option(classLoader.getResource(path.toString))

  def getResourceAsStream(path: Path): Option[InputStream] = Option(classLoader.getResourceAsStream(path.toString))

  private def classLoader = Thread.currentThread.getContextClassLoader

  /**
   * Given a class, generates a list from that class and all of its superclasses.
   */
  def expandSuperclasses(c: Class[_]): List[Class[_]] = if (c != null) c :: expandSuperclasses(c.getSuperclass) else Nil
}

