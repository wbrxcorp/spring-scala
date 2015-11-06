package com.walbrix.scalatra

import java.awt.image.BufferedImage
import java.awt.{Graphics2D, RenderingHints, BasicStroke, Color}
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

import com.walbrix.imaging.ResizeImage
import org.apache.commons.codec.binary.Base64
import org.json4s.{JValue, JObject}
import org.scalatra.{UnsupportedMediaType, BadRequest, Ok}

case class Entity(image:Option[String]=None, title:String = "タイトル", description:String = "説明文")
case class Result(success:Boolean, info:Option[Any]=None)

/**
 * Created by shimarin on 15/10/14.
 */
class EntityWithImageServlet extends org.scalatra.ScalatraServlet with org.scalatra.json.JacksonJsonSupport with com.typesafe.scalalogging.slf4j.LazyLogging {
  override protected implicit def jsonFormats: org.json4s.Formats = org.json4s.DefaultFormats.withBigDecimal ++ org.json4s.ext.JodaTimeSerializers.all

  def drawImage(width:Int, height:Int, format:String)(f:Graphics2D=>Unit):Array[Byte] = {
    val bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    f(g)
    val baos = new ByteArrayOutputStream()
    ImageIO.write(bufferedImage, format, baos)
    baos.toByteArray
  }

  def withJsonObject[T](obj:JValue)(f:JObject=>T):T = obj match {
    case x:JObject =>
      logger.debug(x.toString)
      try {
        f(x)
      }
      catch {
        case e:Throwable =>
          logger.debug("error parsing json object", e)
          halt(BadRequest(e))
      }
    case _ => halt(BadRequest("Request body must be a json object"))
  }

  def imageToDataURI(contentType:String, content:Array[Byte]):String = {
    "data:%s;base64,%s".format(contentType, Base64.encodeBase64String(content))
  }

  def dataURIToImage(dataURI:String):(String,Array[Byte]) = {
    val chunks = dataURI.split(";", 2)
    if (chunks.length != 2 || !chunks(0).startsWith("data:") || !chunks(1).startsWith("base64,")) halt(BadRequest("Invalid Data URI string"))
    try {
      (chunks(0).substring(5), Base64.decodeBase64(chunks(1).substring(7)))
    }
    catch {
      case e:Throwable => halt(BadRequest("Invalid base64 stream"))
    }
  }

  def defaultImage:String = {
    val (width, height, fontSize) = (240, 120, 25.0f)
    val image = drawImage(width, height, "png") { g =>
      val text = "NOW PRINTING"
      val font = g.getFont.deriveFont(fontSize)
      g.setFont(font)
      val bounds = g.getFontMetrics.getStringBounds(text, g)
      //println(bounds.getWidth, bounds.getHeight)
      g.setColor(new Color(128,128,128))
      g.setStroke(new BasicStroke(3))
      g.drawRect(0, 0, width - 1, height - 1)
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      g.setColor(new Color(0,0,0))
      g.drawString(text, ((width-bounds.getWidth)/2).toFloat, (height / 2).toFloat)
    }
    imageToDataURI("image/png", image)
  }

  def noImage:(String, Array[Byte]) = {
    val (width, height, fontSize) = (240, 120, 25.0f)
    val image = drawImage(width, height, "png") { g =>
      val text = "NO IMAGE"
      val font = g.getFont.deriveFont(fontSize)
      g.setFont(font)
      val bounds = g.getFontMetrics.getStringBounds(text, g)
      g.setStroke(new BasicStroke(3))
      g.setColor(new Color(255, 0, 0))
      g.drawLine(0, 0, width, height)
      g.drawLine(width, 0, 0, height)
      g.setColor(new Color(128,128,128))
      g.drawRect(0, 0, width - 1, height - 1)
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      g.setColor(new Color(0,0,0))
      g.drawString(text, ((width-bounds.getWidth)/2).toFloat, (height / 2).toFloat)
    }
    ("image/png", image)
  }

  def entity:Entity = enrichSession(session).getAsOrElse("entity", Entity(Some(defaultImage)))

  get("/") {
    // Accept: application/json の場合はこれがなくてもjsonになるもよう
    //contentType = formats("json")
    entity
  }

  post("/") {
    val entity = withJsonObject(parsedBody) { json =>
      json.extract[Entity]
    }

    session.setAttribute("entity", entity)

    Result(true)
  }

  get("/image") {
    val (contentType, image) = entity.image.map(dataURIToImage(_)).getOrElse(noImage)
    Ok(image, Map("Content-Type" -> contentType))
  }

  post("/image") {
    val requestContentType = request.getContentType
    if (!requestContentType.startsWith("image/")) {
      halt(UnsupportedMediaType("image/* only"))
    }

    try {
      val (image, filetype) = ResizeImage(request.getInputStream, Left(240))
      Ok(imageToDataURI("image/%s".format(filetype), image), Map("Content-Type" -> ("text/plain")))
    }
    catch {
      case e:IllegalArgumentException => BadRequest("Invalid image file")
    }
  }

  get("/no_image") {
    val (contentType, image) = noImage
    Ok(image, Map("Content-Type" -> contentType))
  }
}
