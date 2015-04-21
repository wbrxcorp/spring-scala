package com.walbrix.spring

import collection.JavaConversions._

import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by shimarin on 15/04/18.
 */
object GetRecentDocuments extends com.typesafe.scalalogging.slf4j.LazyLogging {
  /**
   * 指定されたパス以降のリソースで、対応する.mdファイルの更新された日付が新しい順に並べたリストを返す
   */
  def apply(servletContext:javax.servlet.ServletContext, prefix:String):Seq[(String,String,Long)] = {
    val paths = Option(servletContext.getResourcePaths(prefix)).map(_.toSet).getOrElse(Nil).toSet
    val markdownDocs = paths.map { path =>
      val conn = servletContext.getResource(path).openConnection
      (path, conn.getLastModified)
    }.filter(_._1.endsWith(".md")).toSeq.sortBy(-_._2)

    val sources = paths.filter(!_.endsWith(".md"))

    markdownDocs.map { case (mdPath, lastModified) =>
      val prefix = ReplaceFilenameSuffix(mdPath, ".")
      sources.find(_.startsWith(prefix)).headOption.map { correspondingSource =>
        (mdPath, correspondingSource, lastModified)
      }
    }.flatten
  }
}
