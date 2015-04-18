package com.walbrix.spring

import java.io.FileNotFoundException
import javax.servlet.http.HttpServletResponse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{ExceptionHandler, RequestMapping, RequestMethod, RestController}

/**
 * Created by shimarin on 15/04/18.
 */
@RestController
@RequestMapping(Array("manifest"))
class ManifestRequestHandler extends com.typesafe.scalalogging.slf4j.LazyLogging {
  // warファイル内のリソースをロードするには ServletContextが必要
  @Autowired private var servletContext:javax.servlet.ServletContext = _  // SpringによってAutowireされる

  @ExceptionHandler(Array(classOf[FileNotFoundException]))
  def notfound(ex:Exception, response:HttpServletResponse):Unit = {
    response.sendError(HttpServletResponse.SC_NOT_FOUND)
  }

  /**
   * /META-INF/MANIFEST.MFを java.util.jar.Manifestで包んで関数を実行する
   */
  private def withManifest[T](f:java.util.jar.Manifest=>T):Option[T] = {
    Option(servletContext.getResourceAsStream("/META-INF/MANIFEST.MF")).map { is =>
      try (f(new java.util.jar.Manifest(is))) finally(is.close)
    }
  }

  /**
   * MANIFEST.MFの中からいくつかの情報を取得して返す
   */
  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  def get():Map[String,Option[String]] = {
    withManifest { manifest =>
      val mainAttributes = manifest.getMainAttributes
      Seq("Implementation-Title", "Implementation-Vendor", "Implementation-Version").map { field =>
        (field, Option(mainAttributes.getValue(field)))
      }.toMap
    }.getOrElse {
      logger.error("MANIFEST.MF not found")
      throw new FileNotFoundException
    }  // MANIFEST.MFがない場合は 404を出力させる
  }
}
