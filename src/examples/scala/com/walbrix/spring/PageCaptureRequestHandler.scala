package com.walbrix.spring

import javax.servlet.http.HttpServletResponse

import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, RequestMapping}

/**
 * Created by shimarin on 15/04/25.
 */
@Controller
@RequestMapping(Array("pagecapture"))
class PageCaptureRequestHandler extends com.walbrix.spring.ScalikeJdbcSupport with com.typesafe.scalalogging.slf4j.LazyLogging {
  private val permittedDomains = Array("www.walbrix.com", "www.walbrix.net", "www.shimarin.com","www.wbport.com","hiwihhi.com","konozama.link")
  private val defaultURL = "http://www.walbrix.com/jp/"
  private val absoluteURLPattern = """^(https?:\/\/.+)$""".r

  private def saveToCache(url:String, img:Array[Byte]):Unit = {
    update(sql"merge into pagecapture_cache(id,content,created_at) key(id) values(${Sha1Hash(url)},${img},current_timestamp)")
  }

  private def getCache(url:String):Option[Array[Byte]] = {
    single(sql"select content from pagecapture_cache where id=${Sha1Hash(url)}".map(_.blob(1).getBinaryStream()))
      .map(IOUtils.toByteArray(_))
  }

  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  def get(@RequestParam(value="url", required=false) _url:String, @RequestParam(value="mobile", defaultValue="false") mobile:Boolean,
          request:javax.servlet.http.HttpServletRequest, response:HttpServletResponse):Unit = {

    val (url, external) = Option(_url).getOrElse(defaultURL) match {
      case absoluteURLPattern(url) => // 外部サイトの場合
        if (!permittedDomains.contains(new java.net.URL(url).getHost)) {  // 許可リストにないドメインの場合は 403 Forbiddenにする
          response.sendError(HttpServletResponse.SC_FORBIDDEN)
          return
        }
        (url, true)
      case url => // 外部サイトのURLでない場合は、このアプリケーション自身のリソースを示しているものとみなす
        ("%s%s".format(com.walbrix.servlet.GetContextURL(request), url), false)
    }

    logger.debug(url)

    val img = getCache(url).getOrElse { // キャッシュを検索→キャッシュになければ生成
      val img = PageCapture(url, mobile, "png")
      if (!external) saveToCache(url, img)  // 生成したら後のためキャッシュに保存(外部サイトの場合除く)
      img
    }

    response.setContentType("image/png")
    // 長さのわかっているバイナリをレスポンスする場合はきちんと Content-Lengthを付けてやるほうがクライアントが効率よく処理できる
    response.setContentLength(img.length)
    response.getOutputStream.write(img)
  }
}
