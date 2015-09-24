package com.walbrix.spring

import java.io.{InputStream, OutputStream, PrintWriter}
import javax.servlet.ServletContext
import javax.servlet.http.{Cookie, HttpSession, HttpServletResponse, HttpServletRequest}

import com.walbrix.servlet.GetContextURL
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by shimarin on 14/11/15.
 */
trait HttpContextSupport {
  @Autowired private var servletContext:ServletContext = _
  @Autowired private var request:HttpServletRequest = _
  @Autowired private var response:HttpServletResponse = _
  @Autowired private var session:HttpSession = _

  // request
  def getRemoteHost:String = request.getRemoteHost
  def getRemotePort:Int = request.getRemotePort
  def getRemoteAddr:String = request.getRemoteAddr
  def getServerName:String = request.getServerName
  def getContentType:String = request.getContentType
  def getContentLength:Int = request.getContentLength
  def getSession(createNew:Boolean = true):HttpSession = request.getSession(createNew)
  def getAttribute(name:String):Option[AnyRef] = Option(request.getAttribute(name))
  def setAttribute(name:String,value:AnyRef):Unit = request.setAttribute(name, value)
  def removeAttribute(name:String):Unit = request.removeAttribute(name)
  def getCookies:Array[Cookie] = request.getCookies
  def getCookies(name:String):Seq[Cookie] = request.getCookies.filter(_.getName == name)
  def getInputStream:InputStream = request.getInputStream

  // session
  def getSessionAttribute(name:String):Option[AnyRef] = Option(session.getAttribute(name))
  def setSessionAttribute(name:String, value:AnyRef):Unit = session.setAttribute(name, value)
  def removeSessionAttribute(name:String):Unit = session.removeAttribute(name)
  def getSessionId:String = session.getId

  // response
  def addCookie(cookie:Cookie):Unit = response.addCookie(cookie)
  def setContentType(contentType:String):Unit = response.setContentType(contentType)
  def setContentLength(contentLength:Int):Unit = response.setContentLength(contentLength)
  def getWriter:PrintWriter = response.getWriter
  def getOutputStream:OutputStream = response.getOutputStream

  def withOutputStream[T](contentType:Option[String]=None, contentLength:Option[Int]=None)(f:OutputStream=>T):T = {
    contentType.foreach(setContentType(_))
    contentLength.foreach(setContentLength(_))
    val out = getOutputStream
    val rst = f(out)
    out.flush
    rst
  }
  def withOutputStream[T](contentType:String, contentLength:Int)(f:OutputStream=>T):T = withOutputStream(Some(contentType), Some(contentLength))(f)
  def withOutputStream[T](contentType:String)(f:OutputStream=>T):T = withOutputStream(Some(contentType))(f)

  def withWriter[T](contentType:Option[String]=None)(f:PrintWriter=>T):T = {
    contentType.foreach(setContentType(_))
    val writer = getWriter
    val rst = f(writer)
    writer.flush
    rst
  }
  def withWriter[T](contentType:String)(f:PrintWriter=>T):T = withWriter(Some(contentType))(f)

  def getResourceAsStream(path:String):Option[InputStream] = {
    Option(servletContext.getResourceAsStream(path))
  }

  def getContextURL():String = GetContextURL(request)
}
