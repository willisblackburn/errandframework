/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import pages.Page
import org.errandframework.http.Location.thisLocation
import math.{min, max}
import org.errandframework.http._

/**
 * Page displays a selection of page links as a list.
 */
abstract class Pager extends Component {

  def render() = PagerStyle.renderPager(this)

  def count: Int

  def page: Int

  def pageSize: Int

  final def maxPage = (count - 1) / pageSize + 1

  final def previousPage = if (page > 1) Some(min(page - 1, maxPage)) else None

  final def nextPage = if (page < maxPage) Some(max(page + 1, 1)) else None

  def pages = 1 to maxPage

  /**
   * Returns the starting index that the application should provide to the data source.
   * Note that it does not depend on count in any way;  in particular it doesn't try to return only valid
   * start indexes based on the count.  Lucene only provides the result count as a result of a query, so we
   * can't use the result count as an input to the query.
   */
  final def start = (max(page, 1) - 1) * pageSize

  final def firstDisplayed = start + 1

  final def lastDisplayed = min(firstDisplayed + pageSize - 1, count)

  def linkUrl(page: Int): String
}


