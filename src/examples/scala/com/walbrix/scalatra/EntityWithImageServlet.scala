package com.walbrix.scalatra

import java.awt.image.BufferedImage
import java.awt.{Graphics2D, RenderingHints, BasicStroke, Color}
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

import com.walbrix.imaging.ResizeImage
import org.apache.commons.codec.binary.Base64
import org.json4s.{JValue, JObject}
import org.scalatra.{UnsupportedMediaType, BadRequest, Ok, ActionResult}

case class Entity(image:Option[String]=Some("defaultimg.png"))
case class Result(success:Boolean, info:Option[Any]=None)

/**
 * Created by shimarin on 15/10/14.
 */
class EntityWithImageServlet extends org.scalatra.ScalatraServlet with org.scalatra.json.JacksonJsonSupport with com.typesafe.scalalogging.slf4j.LazyLogging {
  override protected implicit def jsonFormats: org.json4s.Formats = org.json4s.DefaultFormats.withBigDecimal ++ org.json4s.ext.JodaTimeSerializers.all

  def drawImage(width:Int, height:Int, format:String)(f:Graphics2D=>Unit):ActionResult = {
    val bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    f(g)
    val baos = new ByteArrayOutputStream()
    ImageIO.write(bufferedImage, format, baos)
    Ok(baos.toByteArray, Map("Content-Type" -> ("image/" + format)))
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
    "data:%s;base64,%s".format(contentType, Base64.encodeBase64URLSafeString(content))
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

  get("/") {
    // Accept: application/json の場合はこれがなくてもjsonになるもよう
    //contentType = formats("json")
    enrichSession(session).getAsOrElse("entity", Entity())
  }

  get("/img") {
    val (width, height, fontSize) = (240, 120, 25.0f)

    drawImage(width, height, "png") { g =>
      val text = "のういめいじ"
      val font = g.getFont.deriveFont(fontSize)
      g.setFont(font)
      val bounds = g.getFontMetrics.getStringBounds(text, g)
      g.setColor(new Color(128, 128, 128))
      g.setStroke(new BasicStroke(3))
      g.drawRect(0, 0, width - 1, height - 1)
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      g.setColor(new Color(0, 0, 0))
      g.drawString(text, ((width - bounds.getWidth) / 2).toFloat, (height / 2).toFloat)
    }
  }

  post("/") {
    val (name, image) = withJsonObject(parsedBody) { json =>
      (
        (json \ "name").extract[String],
        (json \ "image").toSome.map(_.extract[String])
        )
    }

    Result(false)
  }

  get("/upload_image/NO_IMAGE") {
    val (width, height, fontSize) = (240, 120, 25.0f)
    drawImage(width, height, "png") { g =>
      val text = "のういめいじ"
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
  }

  private def imageAttributeName(uuid:String) = "ewi/upload_image/%s".format(uuid)

  get("/upload_image/:uuid") {
    val (image, ct) = enrichSession(session).getAs[(Array[Byte],String)](imageAttributeName(params("uuid"))).getOrElse(halt(404))
    Ok(image, Map("Content-Type"->ct))
  }

  delete("/upload_image/:uuid") {
    enrichSession(session).remove(imageAttributeName(params("uuid")))
    Result(true)
  }

  get("/upload_image") {
    val (width, height, fontSize) = (240, 120, 25.0f)
    drawImage(width, height, "png") { g =>
      val text = "画像削除"
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
  }

  post("/upload_image") {
    val requestContentType = request.getContentType
    if (!requestContentType.startsWith("image/")) {
      halt(415, "image/* only")
    }

    val image = ResizeImage(request.getInputStream, Left(240))

    val uuid = java.util.UUID.randomUUID.toString
    enrichSession(session).put(imageAttributeName(uuid), (image, requestContentType))

    Result(true, Some(uuid))
  }

}
