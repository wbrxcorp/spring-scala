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

  def getRemoteHost():String = request.getRemoteHost
  def getRemotePort():Int = request.getRemotePort
  def getRemoteAddr():String = request.getRemoteAddr
  def getServerName():String = request.getServerName
  def getSession(createNew:Boolean = true):HttpSession = request.getSession(createNew)
  def getAttribute(name:String):AnyRef = request.getAttribute(name)
  def setAttribute(name:String,value:AnyRef):Unit = request.setAttribute(name, value)
  def getCookies():Array[Cookie] = request.getCookies

  def addCookie(cookie:Cookie):Unit = response.addCookie(cookie)
  def setContentType(contentType:String):Unit = response.setContentType(contentType)
  def setContentLength(contentLength:Int):Unit = response.setContentLength(contentLength)
  def getWriter:PrintWriter = response.getWriter
  def getOutputStream:OutputStream = response.getOutputStream

  def getResourceAsStream(path:String):Option[InputStream] = {
    Option(servletContext.getResourceAsStream(path))
  }

  def getContextURL():String = GetContextURL(request)
}
