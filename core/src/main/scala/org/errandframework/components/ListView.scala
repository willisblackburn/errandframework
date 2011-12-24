/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

/**
 * Component that displays items in a list.
 */
abstract class ListView[T](val items: Iterable[T]) extends Component {

  def render() = ListViewStyle.renderListView(this)

  def content(item: T): Component
}

object ListView {

  def apply[T](listViewItems: Iterable[T], listViewComponent: T => Component, listViewBehaviors: Behavior*) = new ListView(listViewItems) {
    def content(item: T) = listViewComponent(item)
    override def behaviors = listViewBehaviors
  }
}



