package com.walbrix.spring.mvc

import javax.servlet.ServletContext
import javax.servlet.http.{Cookie, HttpServletRequest, HttpServletResponse}

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

/**
 * Created by shimarin on 14/11/03.
 */
class AntiXSRFInterceptor extends HandlerInterceptorAdapter {
  private var methodsToBeProtected:Array[String] = Array("POST","DELETE","PUT")
  private var tokenCookieName:String = "XSRF-TOKEN"
  private var tokenHeaderName:String = "X-XSRF-TOKEN"

  def setMethodsToBeProtected(methodsToBeProtected:Array[String]) = this.methodsToBeProtected = methodsToBeProtected
  def setTokenCookieName(tokenCookieName:String) = this.tokenCookieName = tokenCookieName
  def setTokenHeaderName(tokenHeaderName:String) = this.tokenHeaderName = tokenHeaderName

  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: scala.Any): Boolean = {
    response.setHeader("X-Content-Type-Options", "nosniff")
    if (methodsToBeProtected.contains(request.getMethod)) {
      val tokenHeader = Option(request.getHeader(tokenHeaderName)).orElse(
        request.getContentType() match {
          case "application/x-www-form-urlencoded" => Option(request.getParameter(tokenCookieName))
          case _ => None
        }
      )
      val session = Option(request.getSession(false))
      return (tokenHeader, session) match {
        case (Some(header), Some(session)) =>
          header.equals(session.getId())
        case _ =>
          response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token mismatch")
          false
      }
    } else {
      val sessionId =request.getSession(true).getId
      val cookieExists = Option(request.getCookies()).map { cookies =>
        cookies.exists { cookie => cookie.getName().equals(tokenCookieName) && cookie.getValue().equals(sessionId) }
      }.getOrElse(false)
      if (!cookieExists) {
        val cookie: Cookie = new Cookie(tokenCookieName, sessionId)
        cookie.setPath(request.getContextPath match {
          case "" | "/" | null => "/"
          case contextPath => contextPath
        })
        response.addCookie(cookie)
      }
      true
    }
  }
}
