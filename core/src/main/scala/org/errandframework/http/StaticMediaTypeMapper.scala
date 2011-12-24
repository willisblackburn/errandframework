/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * Translates between media types and extensions using a static lookup table.
 */
class StaticMediaTypeMapper(mappings: (MediaType, Seq[String])*) extends MediaTypeMapper {

  require(mappings != null)
  for (mapping <- mappings) {
    require(mapping._1 != null)
    require(mapping._2 != null && mapping._2.length > 0)
  }

  private val mediaTypeMap = mappings.toMap

  private val extensionMap: Map[String, MediaType] = {

    // Invert the map.  The algorithm is:
    // For each extension,
    // find all of the media types,
    // assign each media type a priority based on the position of the extension in that media type's list
    // then store the media type with the highest priority (lowest position) under that extension in the map.

    def bestMediaType(extension: String) = {
      val priorities = mappings.map(m => (m._1, m._2.indexOf(extension)))
      val best = priorities.sortBy(_._2).find(_._2 >= 0).get
      best._1
    }
    val extensions = mappings.flatMap(_._2).distinct
    val extensionMappings = extensions.map(e => (e, bestMediaType(e)))
    extensionMappings.toMap
  }

  def getExtension(mediaType: MediaType) = mediaTypeMap.get(mediaType).map(_.head)

  def getMediaTypeForExtension(extension: Option[String]) = extension.flatMap(extensionMap.get).getOrElse(defaultMediaType)

  def defaultMediaType = MediaType("application/octet-stream")
}