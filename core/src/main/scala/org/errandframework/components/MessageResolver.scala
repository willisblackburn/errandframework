/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import org.errandframework.util.Log
import pages.Page
import java.util.{NoSuchElementException, MissingResourceException, ResourceBundle}
import java.text.MessageFormat
import util.DynamicVariable
import org.errandframework.http.{RequestContext, ErrandServlet}

/**
 * <p>MessageResolver resolves messages and formats messages for components.
 * Given a component and a message key, the resolver looks for a corresponding message by consulting, in order:</p>
 * <ol>
 *   <li>The resource bundle for the servlet and all its superclasses.</li>
 *   <li>The resource bundle for the page and all its superclasses.</li>
 *   <li>The resource bundle for the component and all its superclasses.</li>
 * </ol>
 * <p>If MessageResolver can't find the key in any of those bundles, then it removes one dotted section from
 * the message key and tries again.</p>
 */
object MessageResolver extends Log {

  private val bundleNames = new DynamicVariable[List[String]](Nil)

  def withBundleNames[T](names: List[String])(f: => T) = bundleNames.withValue(bundleNames.value ::: names)(f)

  def formatMessage(key: String)(arguments: Any*): String =
    MessageFormat.format(getMessage(key), arguments.map(_.asInstanceOf[AnyRef]): _*)

  def formatMessage(resourceBundleName: String, key: String)(arguments: Any*): String =
    MessageFormat.format(getMessage(resourceBundleName, key), arguments.map(_.asInstanceOf[AnyRef]): _*)

  def formatMessage(resourceBundleNames: List[String], key: String)(arguments: Any*): String =
    MessageFormat.format(getMessage(resourceBundleNames, key), arguments.map(_.asInstanceOf[AnyRef]): _*)

  def getMessage(key: String): String = getMessage(Nil, key)

  def getMessage(resourceBundleName: String, key: String): String = getMessage(List(resourceBundleName), key)

  def getMessage(resourceBundleNames: List[String], key: String): String = {
    val servletBundleNames = RequestContext.value.map(_.servlet.resourceBundleNames).getOrElse(Nil)
    val pageBundleNames = Page.value.map(_.resourceBundleNames).getOrElse(Nil)
    val bundleNames = servletBundleNames ::: pageBundleNames ::: resourceBundleNames ::: Nil
    log.debug("Looking for message with key: " + key + " in bundles: " + bundleNames)
    val search = for (k <- keyIterator(key);
                      b <- bundleIterator(bundleNames);
                      m <- getString(b, k)) yield m
    if (search.hasNext)
      search.next
    else
      "Message not found: " + key + " (searched " + bundleNames.toString + ")"
  }

  private def keyIterator(key: String) = new Iterator[String] {
    private var _key = key
    def hasNext = _key.length > 0
    def next = {
      if (!hasNext)
        throw new NoSuchElementException("next on empty iterator")
      val nextKey = _key
      _key = _key.lastIndexOf('.') match {
        case -1 => ""
        case i => _key.substring(0, i)
      }
      nextKey
    }
  }

  private def bundleIterator(names: List[String]) = {
    // We know that there won't be bundles for Java library classes, so ignore them.
    def isNotJavaName(name: String) = !name.startsWith("java.") && !name.startsWith("javax.")
    names.filter(isNotJavaName).flatMap(getBundle).iterator
  }

  private def getBundle(name: String): Option[ResourceBundle] = tryOrNone {
    log.debug("Looking for bundle: " + name)
    ResourceBundle.getBundle(name)
  }

  private def getString(bundle: ResourceBundle, key: String): Option[String] = tryOrNone {
    log.debug("Looking for key: " + key + " in bundle: " + bundle)
    bundle.getString(key)
  }

  private def tryOrNone[T](f: => T): Option[T] = try {
    Some(f)
  } catch {
    case _: MissingResourceException => None
  }
}