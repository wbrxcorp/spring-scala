package com.walbrix.imaging

import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

/**
 * Created by shimarin on 15/09/23.
 */
object ResizeImage {
  type Width = Int
  type Height = Int
  type Size = Either[Width, Height]

  /**
   * BufferedImage -> Graphics
   */
  def apply(original:BufferedImage, destination:java.awt.Graphics, width:Int, height:Int, x:Int = 0, y:Int = 0):Unit = {
    val scaled = original.getScaledInstance(width, height, java.awt.Image.SCALE_AREA_AVERAGING)
    destination.drawImage(scaled, x, y, width, height, null)
  }

  /**
   * BufferedImage -> BufferedImage
   */
  def apply(original:BufferedImage, destination:BufferedImage):Unit = {
    apply(original, destination.getGraphics, destination.getWidth, destination.getHeight)
  }

  /**
   * BufferedImage -> new BufferedImage
   */
  def apply(original:BufferedImage, size:Size):BufferedImage = {
    val (origWidth, origHeight) = (original.getWidth, original.getHeight)
    val (width, height) = size match {
      case Left(width) => (width, origHeight * width / origWidth)
      case Right(height) => (origWidth * height / origHeight, height)
    }

    val destination = new BufferedImage(width, height, original.getType)
    apply(original, destination)
    destination
  }

  /**
   * InputStream -> new (BufferedImage, filetype)
   */
  def toBufferedImage(source:InputStream, size:Size):(BufferedImage, String) = {
    // http://stackoverflow.com/questions/21057191/can-i-tell-what-the-file-type-of-a-bufferedimage-originally-was
    val imageInputStream = ImageIO.createImageInputStream(source)
    val readers = ImageIO.getImageReaders(imageInputStream)
    if (!readers.hasNext) throw new IllegalArgumentException("Unsupported image file type")

    val reader = readers.next
    reader.setInput(imageInputStream)
    val original = reader.read(0)

    (apply(original, size), reader.getFormatName)
  }

  /**
   * InputStream -> OutputStream
   * @return filetype
   */
  def apply(source:InputStream, out:java.io.OutputStream, size:Size):String = {
    val (bufferedImage, format) = toBufferedImage(source, size)
    ImageIO.write(bufferedImage, format, out)
    format
  }

  /**
   * InputStream -> new ByteArray
   */
  def apply(source:InputStream, size:Size):Array[Byte] = {
    val baos = new java.io.ByteArrayOutputStream()
    apply(source, baos, size)
    baos.toByteArray
  }

  /**
   * ByteArray -> new ByteArray
   */
  def apply(source:Array[Byte], size:Size):Array[Byte] = apply(new java.io.ByteArrayInputStream(source), size)
}

