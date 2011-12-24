/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import xml.Elem

/**
 * Component that displays tabular data.
 */
abstract class TableView[T](val rows: Iterable[T]) extends Component {

  def render() = TableViewStyle.renderTableView(this)

  def columns: Seq[Column]

  trait Column {

    def headerContent: Component

    def content(row: T): Component

    def behaviors: Seq[Behavior] = Seq.empty

    def transform(elem: Elem) = Behavior.transform(elem, behaviors: _*)
  }

  object Column {

    def apply(columnHeaderContent: Component, columnContent: T => Component, columnBehaviors: Behavior*) = new Column {

      def headerContent = columnHeaderContent

      def content(row: T) = columnContent(row)

      override def behaviors = columnBehaviors
    }
  }

  def rowCssClass(number: Int): Option[String] = Some(if (number % 2 == 0) "even" else "odd")
}
