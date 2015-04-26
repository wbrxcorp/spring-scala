package com.walbrix.spring

import javax.imageio.ImageIO

/**
 * Created by shimarin on 15/04/26.
 */
object PageCapture {
  class PageCaptureException(cmdline:Seq[String], code:Int) extends
    Exception("Subprocess '%s' returned exit code %d".format(cmdline.mkString(" "), code)) {
  }

  /**
   * キャプチャした画像を指定の寸法を持つ正方形にリサイズする。リサイズはアスペクト比を維持して横幅基準に行われ、
   * 縦方向は切り抜かれるか空白になる
   */
  private def resizeAndCrop(is:java.io.InputStream, os:java.io.OutputStream, size:Int, format:String):Unit = {
    val original = ImageIO.read(is)
    val bufferedImage = new java.awt.image.BufferedImage(size, size, original.getType)
    val scaled = original.getScaledInstance(size, -1, java.awt.Image.SCALE_AREA_AVERAGING)
    bufferedImage.getGraphics.drawImage(scaled, 0, 0, null)
    ImageIO.write(bufferedImage, format, os)
  }

  /**
   * wkhtmltoimageコマンドを使って指定されたURLのスクリーンキャプチャを得る
   * mobile == trueの場合、横幅320pxのブラウザがシミュレートされる(通常はwkhtmltoimageのデフォルトで1024px)
   */
  def apply(url:String, mobile:Boolean = false, format:String = "png", size:Int = 200):Array[Byte] = {
    val cmdline = Seq("wkhtmltoimage","-f","png","--javascript-delay","500") ++ (if (mobile) Seq("--width","320","--disable-smart-width") else Nil) ++ Seq(url, "-")
    val os = new java.io.ByteArrayOutputStream
    val pio = new scala.sys.process.ProcessIO(
      _ => (),
      { stdout => resizeAndCrop(stdout, os, size, format) },
      _ => ())
    scala.sys.process.Process(cmdline).run(pio).exitValue match {
      case 0 => os.toByteArray
      case x => throw new PageCaptureException(cmdline, x)
    }
  }
}
