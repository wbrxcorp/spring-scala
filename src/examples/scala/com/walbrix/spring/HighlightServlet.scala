package com.walbrix.spring

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringEscapeUtils
import org.pegdown.{Extensions, PegDownProcessor}

/**
 * Created by shimarin on 15/03/13.
 */
class HighlightServlet extends HttpServlet with LazyLogging {
  private var template:Option[String] = None

  override def init():Unit = {
    template = Option(getInitParameter("template"))
  }

  override def doGet(request:HttpServletRequest, response:HttpServletResponse):Unit = {
    logger.debug("servletPath:%s, pathInfo:%s".format(request.getServletPath, request.getPathInfo))

    val resourcePath = request.getServletPath + Option(request.getPathInfo).getOrElse("")
    val is = Option(getServletContext.getResourceAsStream(resourcePath)).getOrElse {
      response.sendError(HttpServletResponse.SC_NOT_FOUND)
      return
    }
    val content = StringEscapeUtils.escapeHtml(IOUtils.toString(is, "UTF-8"))
    template match {
      case Some(x) =>
        request.setAttribute("content", content)
        request.getRequestDispatcher(x).forward(request, response)
      case None =>
        response.setContentType("text/html; charset=UTF-8")
        val writer = response.getWriter
        writer.write("<html>\n<head>\n<title>Source</title>\n")
        writer.write("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/styles/github.min.css\">\n<script src=\"http://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/highlight.min.js\"></script>\n<script>hljs.initHighlightingOnLoad();</script>")
        writer.write("</head>\n<body><pre><code>")
        writer.write(content)
        writer.write("</code></pre></body>\n</html>")
        writer.flush()
    }
  }
}
