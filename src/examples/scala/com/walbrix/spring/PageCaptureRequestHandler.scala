package com.walbrix.spring

import javax.imageio.ImageIO
import javax.servlet.http.HttpServletResponse

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, RequestMapping}

/**
 * Created by shimarin on 15/04/25.
 */
@Controller
@RequestMapping(Array("pagecapture"))
class PageCaptureRequestHandler {

  private val permittedDomains = Array("www.walbrix.com", "www.walbrix.net", "www.shimarin.com","www.wbport.com")
  private val defaultURL = "http://www.walbrix.com/jp/"

  private def resizeAndCrop(is:java.io.InputStream, os:java.io.OutputStream, size:Int = 200, format:String = "png"):Unit = {
    val original = ImageIO.read(is)
    val bufferedImage = new java.awt.image.BufferedImage(size, size, original.getType)
    val scaled = original.getScaledInstance(size, -1, java.awt.Image.SCALE_AREA_AVERAGING)
    bufferedImage.getGraphics.drawImage(scaled, 0, 0, null)
    ImageIO.write(bufferedImage, format, os)
  }

  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  def get(@RequestParam(value="url", required=false) _url:String, response:HttpServletResponse):Unit = {

    val url = Option(_url).getOrElse(defaultURL)

    val domain = new java.net.URL(url).getHost
    if (!permittedDomains.contains(domain)) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN)
      return
    }

    // http://stackoverflow.com/questions/6013415/how-does-the-scala-sys-process-from-scala-2-9-work
    val pb = scala.sys.process.Process(Seq("wkhtmltoimage","-f","png",url,"-"))
    response.setContentType("image/png")
    val out = response.getOutputStream
    val pio = new scala.sys.process.ProcessIO(_ => (),
      stdout => resizeAndCrop(stdout, out) ,
      _ => ())
    pb.run(pio).exitValue()
  }
}
