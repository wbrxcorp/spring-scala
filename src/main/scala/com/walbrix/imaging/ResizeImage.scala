package com.walbrix.imaging

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, OutputStream, InputStream}
import javax.imageio.ImageIO

/**
 * Created by shimarin on 15/09/23.
 */
object ResizeImage {
  type Width = Int
  type Height = Int

  def toBufferedImage(source:InputStream, size:Either[Width, Height]):(BufferedImage, String) = {
    val readers = ImageIO.getImageReaders(source)
    if (!readers.hasNext) throw new IllegalArgumentException("Unsupported image file type")

    val reader = readers.next
    val original = reader.read(0)

    val (width, height) = size match {
      case Left(width) => (1, 2)
      case Right(height) => (2, 3)
    }

    val bufferedImage = new BufferedImage(width, height, original.getType)
    val scaled = original.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING)
    bufferedImage.getGraphics.drawImage(scaled, 0, 0, width, height, null)
    (bufferedImage, reader.getFormatName)
  }

  def apply(source:InputStream, out:OutputStream, size:Either[Width,Height]):Unit = {
    val (bufferedImage, format) = toBufferedImage(source, size)
    ImageIO.write(bufferedImage, format, out)
  }

  def apply(source:InputStream, size:Either[Width, Height]):Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    apply(source, baos, size)
    baos.toByteArray
  }

  def apply(source:Array[Byte], size:Either[Width, Height]):Array[Byte] = {
    val bais = new ByteArrayInputStream(source)
    apply(bais, size)
  }


}
// http://stackoverflow.com/questions/21057191/can-i-tell-what-the-file-type-of-a-bufferedimage-originally-was
