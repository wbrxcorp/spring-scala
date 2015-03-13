package com.walbrix.markdown

import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}

import org.apache.commons.io.IOUtils
import org.pegdown.{PegDownProcessor, Extensions}

/**
 * Created by shimarin on 15/03/13.
 */
class MarkdownServlet extends HttpServlet {
  private var template:Option[String] = None

  override def init():Unit = {
    template = Option(getInitParameter("template"))
  }

  override def doGet(request:HttpServletRequest, response:HttpServletResponse):Unit = {
    val is = Option(getServletContext.getResourceAsStream(request.getServletPath)).getOrElse {
      response.sendError(HttpServletResponse.SC_NOT_FOUND)
      return
    }
    val content = new PegDownProcessor(Extensions.ALL).markdownToHtml(IOUtils.toString(is, "UTF-8"))
    template match {
      case Some(x) =>
        request.setAttribute("content", content)
        request.getRequestDispatcher(x).forward(request, response)
      case None =>
        response.setContentType("text/html; charset=UTF-8")
        val writer = response.getWriter
        writer.write("<html>\n<head>\n<title>Markdown page</title>\n</head>\n<body>")
        writer.write(content)
        writer.write("</body>\n</html>")
        writer.flush()
    }
  }
}
