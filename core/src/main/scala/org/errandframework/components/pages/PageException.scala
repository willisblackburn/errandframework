/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components
package pages

/**
 * Class of exceptions involving pages.
 */
class PageException(val page: Page) extends Exception

class InvisiblePageException(page: Page) extends PageException(page)

class DisabledPageException(page: Page) extends PageException(page)
