package com.walbrix.spring

import collection.JavaConversions._

import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by shimarin on 15/04/18.
 */
trait RecentDocument extends com.typesafe.scalalogging.slf4j.LazyLogging {
  @Autowired private var servletContext:javax.servlet.ServletContext = _

  /**
   * warファイル内のprefixで指定されたパス以下に存在するリソースの名前を全て得る
   */
  def getResourcePaths(prefix:String):Set[String] = {
    // 該当ファイルが無い場合 getResourcePathsは空のコレクションではなくnullを返すのでnullの場合も考慮する必要がある
    Option(servletContext.getResourcePaths(prefix)).map(_.toSet).getOrElse(Nil).toSet
  }

  /**
   * 指定されたパス以降のリソースで、対応する.mdファイルの更新された日付が新しい順に並べたリストを返す
   */
  def getRecentDocuments(prefix:String):Seq[(String,Long)] = {
    val paths = getResourcePaths(prefix)
    val markdownDocs = paths.map { path =>
      val conn = servletContext.getResource(path).openConnection
      (path, conn.getLastModified)
    }.filter(_._1.endsWith(".md")).toSeq.sortBy(-_._2)

    val sources = paths.filter(!_.endsWith(".md"))

    markdownDocs.map { case (mdPath, lastModified) =>
      val prefix = ReplaceFilenameSuffix(mdPath, ".")
      sources.find(_.startsWith(prefix)).headOption.map { correspondingSource =>
        (correspondingSource, lastModified)
      }
    }.flatten
  }
}
