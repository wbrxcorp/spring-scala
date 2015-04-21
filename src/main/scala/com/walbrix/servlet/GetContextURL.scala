package com.walbrix.servlet

/**
 * Created by shimarin on 15/04/21.
 */
object GetContextURL {
  def apply(request:javax.servlet.http.HttpServletRequest):String = {
    val buf = new StringBuilder
    val scheme = request.isSecure match {
      case true => ("https", 443)
      case false => ("http", 80)
    }
    buf.append(scheme._1)
    buf.append("://")
    buf.append(request.getServerName)
    buf.append(
      request.getServerPort match {
        case port if port != scheme._2 => ":%d".format(port)
        case _ => ""
      }
    )
    buf.append(request.getContextPath)
    buf.toString()
  }
}
