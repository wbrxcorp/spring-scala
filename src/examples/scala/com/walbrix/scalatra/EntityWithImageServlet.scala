package com.walbrix.scalatra

import java.awt.image.BufferedImage
import java.awt.{Graphics2D, RenderingHints, BasicStroke, Color}
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

import com.walbrix.imaging.ResizeImage
import org.json4s.JObject
import org.scalatra.{Ok, ActionResult}

case class Entity(image:Option[String]=Some("defaultimg.png"))
case class Result(success:Boolean, info:Option[Any]=None)

/**
 * Created by shimarin on 15/10/14.
 */
class EntityWithImageServlet extends org.scalatra.ScalatraServlet with org.scalatra.json.JacksonJsonSupport {
  override protected implicit def jsonFormats: org.json4s.Formats = org.json4s.DefaultFormats.withBigDecimal ++ org.json4s.ext.JodaTimeSerializers.all

  def drawImage(width:Int, height:Int, format:String)(f:Graphics2D=>Unit):ActionResult = {
    val bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    f(g)
    val baos = new ByteArrayOutputStream()
    ImageIO.write(bufferedImage, format, baos)
    Ok(baos.toByteArray, Map("Content-Type" -> ("image/" + format)))
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
    parsedBody match {
      case json:JObject =>
        val name = (json \ "name").extract
        val image = (json \ "image").toSome.map (_.extract[String] )
      case _ => halt(400, "Request body must be a json object")
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
