/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.components

import org.errandframework.http.{RequestContext, HttpHelpers}

/**
 * Base class for components that display images.
 * The images may be static or dynamic.
 */
abstract class Image extends Component {

  def render() = ImageStyle.renderImage(this)

  def url: String

  /**
   * An alternate description of the image.
   * Rendered as the image's alt attribute.
   * This description will be used by text-mode browsers or speech applications.
   */
  def alt: Option[String] = None

  /**
   * The title of the image.
   * Rendered as the image's title attribute.
   * Browsers often display the title as a hover-over.
   */
  def title: Option[String] = None

  def width: Option[Int] = None

  def height: Option[Int] = None
}

object Image {

  def apply(imageAlt: Option[String], imageTitle: Option[String], imageWidth: Option[Int], imageHeight: Option[Int],
            imageUrl: String, imageBehaviors: Behavior*) = new Image {

    val url = imageUrl

    override def title = imageTitle

    override def alt = imageAlt

    override def width = imageWidth

    override def height = imageHeight

    override def behaviors = imageBehaviors
  }

  def apply(imageAlt: String, imageTitle: String, imageWidth: Int, imageHeight: Int, imageUrl: String, imageBehaviors: Behavior*): Image =
    apply(Some(imageTitle), Some(imageAlt), Some(imageWidth), Some(imageHeight), imageUrl, imageBehaviors: _*)

  def apply(imageAlt: String, imageWidth: Int, imageHeight: Int, imageUrl: String, imageBehaviors: Behavior*): Image =
    apply(None, Some(imageAlt), Some(imageWidth), Some(imageHeight), imageUrl, imageBehaviors: _*)

  def apply(imageWidth: Int, imageHeight: Int, imageUrl: String, imageBehaviors: Behavior*): Image =
    apply(None, None, Some(imageWidth), Some(imageHeight), imageUrl, imageBehaviors: _*)

  def apply(imageAlt: String, imageTitle: String, imageUrl: String, imageBehaviors: Behavior*): Image =
    apply(Some(imageTitle), Some(imageAlt), None, None, imageUrl, imageBehaviors: _*)

  def apply(imageAlt: String, imageUrl: String, imageBehaviors: Behavior*): Image =
    apply(None, Some(imageAlt), None, None, imageUrl, imageBehaviors: _*)

  def apply(imageUrl: String, imageBehaviors: Behavior*): Image =
    apply(None, None, None, None, imageUrl, imageBehaviors: _*)
}