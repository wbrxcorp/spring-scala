package com.walbrix.spring


import java.awt.{BasicStroke, Stroke, RenderingHints, Color}

import com.fasterxml.jackson.databind.JsonNode
import com.walbrix.imaging.ResizeImage
import org.apache.commons.io.IOUtils
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(Array("entitywithimage"))
class EntityWithImageRequestHandler extends com.walbrix.spring.mvc.HttpErrorStatus with HttpContextSupport with ServerSideImagingSupport {

  case class Entity()
  case class Result(success:Boolean, info:Option[Any]=None)

  private def imageAttributeName(uuid:String) = "ewi/upload_image/%s".format(uuid)

  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  def entity():Entity = {
    Entity(
    )
  }

  @RequestMapping(value=Array("img"), method=Array(RequestMethod.GET))
  def img():Unit = {
    val (width, height, fontSize) = (240, 120, 25.0f)
    drawAndOutputImage(width, height, "png", true) { g =>
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

  @RequestMapping(value=Array(""), method=Array(RequestMethod.POST))
  def entity(@RequestBody body:JsonNode):Result = {
    Result(false)
  }

  @RequestMapping(value=Array("upload_image/NO_IMAGE"), method=Array(RequestMethod.GET))
  def noimg():Unit = {
    val (width, height, fontSize) = (240, 120, 25.0f)
    drawAndOutputImage(width, height, "png", true) { g =>
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

  @RequestMapping(value=Array("upload_image/{uuid}"), method=Array(RequestMethod.GET))
  def uploadImg(@PathVariable uuid:String):Unit = {
    val (image, contentType) = getSessionAttribute(imageAttributeName(uuid)).map(_.asInstanceOf[(Array[Byte],String)]).getOrElse(raiseNotFound("Invalid UUID"))

    withOutputStream(contentType, image.length)(_.write(image))
  }

  @RequestMapping(value=Array("upload_image/{uuid}"), method=Array(RequestMethod.DELETE))
  def deleteUploadImg(@PathVariable uuid:String):Unit = {
    removeSessionAttribute(imageAttributeName(uuid))
    println("deleted")
    Result(true)
  }

  @RequestMapping(value=Array("upload_image"), method=Array(RequestMethod.GET))
  def uploadImgNull():Unit = {
    val (width, height, fontSize) = (240, 120, 25.0f)
    drawAndOutputImage(width, height, "png", true) { g =>
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

  @RequestMapping(value=Array("upload_image"), method=Array(RequestMethod.POST),consumes=Array("image/*"))
  def uploadImg():Result = {
    val contentType = getContentType

    if (!contentType.startsWith("image/")) raiseBadRequest("Invalid Content-Type")

    val image = ResizeImage(getInputStream, Left(240))

    val uuid = java.util.UUID.randomUUID.toString
    setSessionAttribute(imageAttributeName(uuid), (image, contentType))

    Result(true, Some(uuid))
  }

}
