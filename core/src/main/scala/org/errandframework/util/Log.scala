/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.util


import org.slf4j.LoggerFactory

/**
 * Simple trait to provide a logger for classes that need one.
 * Uses lazy initialization to avoid creating loggers when they aren't needed.
 */
trait Log {

  lazy val log = LoggerFactory.getLogger(getClass)
}