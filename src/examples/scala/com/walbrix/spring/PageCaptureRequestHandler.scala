package com.walbrix.spring

import javax.imageio.ImageIO
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

  private def resizeAndCrop(is:java.io.InputStream, size:Int = 200, format:String = "png"):Array[Byte] = {
    val original = ImageIO.read(is)
    val bufferedImage = new java.awt.image.BufferedImage(size, size, original.getType)
    val scaled = original.getScaledInstance(size, -1, java.awt.Image.SCALE_AREA_AVERAGING)
    bufferedImage.getGraphics.drawImage(scaled, 0, 0, null)
    val os = new java.io.ByteArrayOutputStream()
    ImageIO.write(bufferedImage, format, os)
    os.toByteArray
  }

  private def saveToCache(url:String, img:Array[Byte]):Unit = {
    update(sql"merge into pagecapture_cache(id,content,created_at) key(id) values(${Sha1Hash(url)},${img},current_timestamp)")
  }

  private def getCache(url:String):Option[Array[Byte]] = {
    single(sql"select content from pagecapture_cache where id=${Sha1Hash(url)}".map { row =>
      IOUtils.toByteArray(row.blob("content").getBinaryStream)
    })
  }

  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  def get(@RequestParam(value="url", required=false) _url:String, @RequestParam(value="mobile", defaultValue="false") mobile:Boolean,
          request:javax.servlet.http.HttpServletRequest, response:HttpServletResponse):Unit = {

    val (url, external) = Option(_url).getOrElse(defaultURL) match {
      case absoluteURLPattern(url) =>
        val domain = new java.net.URL(url).getHost
        if (!permittedDomains.contains(domain)) {
          response.sendError(HttpServletResponse.SC_FORBIDDEN)
          return
        }
        (url, true)
      case url =>
        val absoluteURL = "%s%s".format(com.walbrix.servlet.GetContextURL(request), url)
        logger.debug(absoluteURL)
        (absoluteURL, false)
    }

    logger.debug(url)

    response.setContentType("image/png")
    val out = response.getOutputStream

    getCache(url) match {
      case Some(img) =>
        response.setContentLength(img.length)
        out.write(img)
      case None =>
        // http://stackoverflow.com/questions/6013415/how-does-the-scala-sys-process-from-scala-2-9-work
        val cmdline = Seq("wkhtmltoimage","-f","png") ++ (if (mobile) Seq("--width","320","--disable-smart-width") else Nil) ++ Seq(url, "-")
        val pb = scala.sys.process.Process(cmdline)
        val pio = new scala.sys.process.ProcessIO(_ => (),
        { stdout =>
          val img = resizeAndCrop(stdout)
          if (!external) saveToCache(url, img)
          response.setContentLength(img.length)
          out.write(img)
        },
        _ => ())
        pb.run(pio).exitValue()
    }
  }
}
