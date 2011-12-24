/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import xml.NodeSeq
import util.DynamicVariable
import org.errandframework.util.XmlHelpers._

/**
 * Table renderer.
 */
trait TableViewStyle {

  def renderTableView[T](component: TableView[T]): NodeSeq
}

object TableViewStyle extends DynamicVariable[TableViewStyle](DefaultTableViewStyle) with TableViewStyle {

  def renderTableView[T](component: TableView[T]) = value.renderTableView(component)
}

object DefaultTableViewStyle extends TableViewStyle {

  def renderTableView[T](component: TableView[T]) = {
    component.transform(<table class="errand-TableView">
      <thead>
        {for (c <- component.columns) yield c.headerContent.renderIfVisible(c.transform(<th>{c.headerContent.render}</th>))}
      </thead>
      <tbody>
        {for ((r, number) <- component.rows.zipWithIndex) yield <tr class={asText(component.rowCssClass(number))}>{for (c <- component.columns) yield c.headerContent.renderIfVisible(c.transform(<td>{c.content(r).render}</td>))}</tr>}
      </tbody>
    </table>)
  }
}
