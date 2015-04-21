package com.walbrix.spring

import com.walbrix.servlet.GetContextURL
import org.apache.commons.io.IOUtils

import collection.JavaConversions._

/**
 * Created by shimarin on 15/04/21.
 */
class RSSServlet extends javax.servlet.http.HttpServlet with com.typesafe.scalalogging.slf4j.LazyLogging {
  var servletContext:javax.servlet.ServletContext = _
  var contextRoot:String = _
  var title = "ワルブリックス株式会社 実験場"
  var link = "http://www.walbrix.com/spring-scala/"
  var description =
    "ワルブリックス株式会社が Spring Framework, Scala, AngularJSなどについて色々実験している場所です。各ソースコードに解説文を付けて公開しています。"

  override def init():Unit = {
    this.servletContext = this.getServletContext
    contextRoot = servletContext.getContextPath
    Option(servletContext.getInitParameter("title")).foreach(this.title = _)
    Option(servletContext.getInitParameter("link")).foreach(this.link = _)
    Option(servletContext.getInitParameter("description")).foreach(this.description = _)
  }

  override def doGet(request:javax.servlet.http.HttpServletRequest, response:javax.servlet.http.HttpServletResponse):Unit = {
    val feed = new com.rometools.rome.feed.synd.SyndFeedImpl
    feed.setFeedType("rss_2.0");

    feed.setTitle(title);
    feed.setLink(link);
    feed.setDescription(description);

    val entries = GetRecentDocuments(servletContext, "/").map { case (md, source, timestamp) =>
      val is = servletContext.getResourceAsStream(md)
      val (content, meta) =
        ExtractMetadataFromMarkdown(try(org.apache.commons.io.IOUtils.toString(is, "UTF-8")) finally(is.close))
      meta.get("title").map { title =>
        val entry = new com.rometools.rome.feed.synd.SyndEntryImpl()
        entry.setTitle(title)
        entry.setLink(GetContextURL(request) + source)
        entry.setPublishedDate(new java.util.Date(timestamp))
        // RSS2.0の Itemでは titleか descriptionのどちらかが必須
        // 先の条件で titleを必須にしているので descriptionはオプションで良い
        meta.get("description").foreach { description =>
          val content = new com.rometools.rome.feed.synd.SyndContentImpl
          content.setType("text/plain")
          content.setValue(description)
          entry.setDescription(content)
        }
        entry
      }
    }.flatten

    feed.setEntries(entries)

    response.setContentType("Content-type: text/xml;charset=utf-8")
    val writer = response.getWriter
    try(new com.rometools.rome.io.SyndFeedOutput().output(feed, writer)) finally(writer.close)
  }
}
