package com.walbrix.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{RequestMapping, RestController}

/**
 * Created by shimarin on 15/04/26.
 */
case class Entry(title:String, link:String, lastUpdate:java.util.Date, description:Option[String])

@RestController
@RequestMapping(value=Array("recent"))
class RecentDocumentRequestHandler {
  @Autowired private var servletContext:javax.servlet.ServletContext = _

  @RequestMapping(value=Array(""))
  def get():Seq[Entry] = {
    GetRecentDocuments(servletContext, "/src/").map { case (md, source, timestamp) =>
      val is = servletContext.getResourceAsStream(md)
      val (content, meta) =
        ExtractMetadataFromMarkdown(try(org.apache.commons.io.IOUtils.toString(is, "UTF-8")) finally(is.close))
      meta.get("title").map { title =>
        Entry(title, source, new java.util.Date(timestamp), meta.get("description"))
      }
    }.flatten match {
      case Nil => Seq(Entry("ダミー", "DUMMY_LINK", new java.util.Date(), Some("ダミーの説明")))
      case x => x
    }
  }
}
