package com.walbrix.spring

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, InputStream, ByteArrayOutputStream}
import javax.imageio.ImageIO

import com.walbrix.imaging.ResizeImage

/**
 * Created by shimarin on 15/09/23.
 */
trait ServerSideImagingSupport extends HttpContextSupport {
  val FORMAT_PNG = "png"
  val FORMAT_JPEG = "jpeg"

  def outputImage(bufferedImage:BufferedImage, format:String, bufferedOutput:Boolean = false):Unit = {
    if (bufferedOutput) { // 一度イメージをbytearrayにし、content-lengthとともにレスポンスする
    val baos = new ByteArrayOutputStream()
      ImageIO.write(bufferedImage, format, baos)
      withOutputStream("image/%s".format(format), baos.size())(_.write(baos.toByteArray))
    } else { // content-lengthなしでストリームに対しイメージ生成を行う
      withOutputStream("image/%s".format(format))(ImageIO.write(bufferedImage, format, _))
    }
  }

  def drawAndOutputImage[T](width:Int, height:Int, format:String, bufferedOutput:Boolean = false)(f:Graphics2D=>T):T = {
    val bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val rst = f(bufferedImage.getGraphics.asInstanceOf[Graphics2D])

    outputImage(bufferedImage, format, bufferedOutput)

    rst
  }

  def outputResizedImage(source:InputStream, size:ResizeImage.Size, bufferedOutput:Boolean = false):Unit = {
    val (bufferedImage, format) = ResizeImage.toBufferedImage(source, size)
    outputImage(bufferedImage, format, bufferedOutput)
  }
}
