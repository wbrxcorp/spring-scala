package com.walbrix.spring

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.apache.http.{HttpResponse, HttpStatus}
import org.apache.http.impl.client.HttpClients
import org.joda.time.DateTime
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation._

import collection.JavaConversions._

/**
 * Created by shimarin on 15/04/19.
 */

case class Canonical(url:String, pageName:Option[String])

object Canonical {
  private val urlPattern = """^http:\/\/ja\.wikipedia\.org\/wiki\/(.+)$""".r

  def apply(url:String):Canonical = {
    Canonical(url, url match {
      case urlPattern(x) => Some(java.net.URLDecoder.decode(x, "UTF-8"))
      case _ => None
    })
  }
}

case class WikipediaEntry(
      title:String, // 項目タイトル
      content:Option[String], // ページ内容（最初の段落）
      canonical:Option[Canonical], // ページの正式なURLと項目名
      lastModified:DateTime,  // Wikipedia上での最終更新時刻
      fetchedAt:Option[DateTime] = None // キャッシュに保存された時刻
)

class HttpStatusCodeIsNot200Exception(val code:Int) extends RuntimeException {}

@RestController
@RequestMapping(Array("wikipedia"))
class WikipediaRequestHandler extends ScalikeJdbcSupport with LazyLogging {

  type Closable = { def close():Unit }
  def using[A <: Closable,B]( resource:A )( f:A => B ) = try(f(resource)) finally(resource.close)

  private val urlPattern = """^http:\/\/ja\.wikipedia\.org\/wiki\/(.+)$""".r

  private def createURL(pageName:String):String =
    "http://ja.wikipedia.org/wiki/%s".format(java.net.URLEncoder.encode(pageName, "UTF-8"))

  private def getLastModified(response: org.apache.http.HttpResponse):Option[DateTime] = {
    Option(response.getFirstHeader("Last-Modified")).map { lastModified =>
      new DateTime(org.apache.http.client.utils.DateUtils.parseDate(lastModified.getValue))
    }
  }

  private def ensureOK(response:HttpResponse):Unit = {
    val statusCode = response.getStatusLine.getStatusCode
    if (statusCode != HttpStatus.SC_OK) throw new HttpStatusCodeIsNot200Exception(statusCode)
  }

  def head(pageName:String):Option[DateTime] = {
    val url = createURL(pageName)
    using(HttpClients.createDefault()) { httpClient =>
      logger.debug("Fetching: %s".format(url))
      using(httpClient.execute(new org.apache.http.client.methods.HttpHead(url))) { response =>
        ensureOK(response)
        getLastModified(response)
      }
    }
  }

  def parse(pageName:String):WikipediaEntry = {
    val url = createURL(pageName)
    using(HttpClients.createDefault()) { httpClient =>
      logger.debug("Fetching: %s".format(url))
      using(httpClient.execute(new org.apache.http.client.methods.HttpGet(url))) { response =>
        // HTTPステータスコードが200であることを確認（そうでない場合例外を送出する）
        ensureOK(response)
        using(response.getEntity.getContent) { is =>
          val soup = org.jsoup.Jsoup.parse(is, "UTF-8", url)
          WikipediaEntry(
            // <h1 id="firstHeading">...</h1>
            title = soup.getElementById("firstHeading").text(),
            // <div id="mw-content-text"><p>...</p></div>
            content = soup.getElementById("mw-content-text").children().find(_.tagName.equals("p")).map(_.text().trim),
            // <link rel="canonical" href="...">
            canonical = soup.getElementsByTag("link")
              .find(elem => elem.hasAttr("rel") && elem.attr("rel").equals("canonical") && elem.hasAttr("href"))
              .map(link => Canonical(link.attr("href"))),
            // HTTPレスポンスヘッダから Last-Modifiedを取得（ない場合は現在時刻を入れておく）
            lastModified = getLastModified(response).getOrElse(new DateTime())
          )
        }
      }
    }
  }

  /**
   * WikipediaのWebサーバから200以外のコードが返ってきた場合は例外に乗せてスローし、クライアントにそのままの
   * ステータスを返す
   */
  @ExceptionHandler(value=Array(classOf[HttpStatusCodeIsNot200Exception]))
  def exception(ex:HttpStatusCodeIsNot200Exception, response:HttpServletResponse):Unit = {
    logger.error("HTTP Status is not 200(OK)", ex)
    response.sendError(ex.code)
  }

  /**
   * クライアントから与えられた項目名でWikipediaから情報をフェッチして返すAPI
   */
  @Transactional
  @RequestMapping(value=Array("{pageName:.+}"), method=Array(RequestMethod.GET))
  def get(@PathVariable pageName:String, request:HttpServletRequest):WikipediaEntry = {
    logger.debug(pageName)
    // まずはキャッシュを検索(キャッシュの賞味期限は1週間とする)
    single(sql"select * from wikipedia_cache where page_name=${pageName} and current_timestamp - fetched_at < 7".map { row =>
      logger.debug("Cache hit.")
      WikipediaEntry(
        row.string("title"),
        row.stringOpt("content"),
        row.stringOpt("canonical_url").map(Canonical(_)),
        row.jodaDateTime("last_modified"),
        row.jodaDateTimeOpt("fetched_at")
      )
      /* 本当は賞味期限切れのキャッシュに対してもHEADリクエストをWikipediaに送ってLast-Modifiedを確認することで
         賞味期限の延長を認める処理をするほうが理想的 */
    }).getOrElse {
      // キャッシュになければHTTPでWikipediaからフェッチ
      val ipAddress = request.getRemoteAddr

      // 1秒以内に複数回 Wikipediaへのフェッチを発生させようとするユーザーを弾く
      if (exists(sqls"wikipedia_clients where ip_address=${ipAddress} and datediff('SECOND', fetched_at, current_timestamp) < 1")) {
        update(sql"update wikipedia_clients set fetched_at=current_timestamp where ip_address=${ipAddress}")
        throw new HttpStatusCodeIsNot200Exception(HttpStatus.SC_FORBIDDEN)
      }
      // 短時間の大量フェッチを阻止するために、Wikipediaからのフェッチを発生させたユーザーのIPアドレスを記録する
      update(sql"""MERGE INTO wikipedia_clients(ip_address,fetched_at) KEY(ip_address) values(${ipAddress}, current_timestamp)""")
      val entry = parse(pageName)
      // フェッチしたコンテンツはキャッシュに保存する
      update(
        sql"""MERGE INTO wikipedia_cache(page_name,title,content,canonical_url,last_modified,fetched_at) KEY(page_name)
          VALUES(${pageName}, ${entry.title},${entry.content},${entry.canonical.map(_.url)},${entry.lastModified}, current_timestamp)""")
      logger.debug("Cache updated.")
      entry
    }
  }
}
