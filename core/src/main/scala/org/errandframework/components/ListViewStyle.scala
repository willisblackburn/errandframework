/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

import xml.NodeSeq
import util.DynamicVariable
import org.errandframework.util.XmlHelpers._

/**
 * List renderer.
 */
trait ListViewStyle {

  def renderListView[R](component: ListView[R]): NodeSeq
}

object ListViewStyle extends DynamicVariable[ListViewStyle](DefaultListViewStyle) with ListViewStyle {

  def renderListView[R](component: ListView[R]) = value.renderListView(component)
}

object DefaultListViewStyle extends ListViewStyle {

  def renderListView[R](component: ListView[R]) = {
    component.transform(<ul class="errand-ListView">
      {for (i <- component.items) yield <li>{component.content(i).render}</li>}
    </ul>)
  }
}
