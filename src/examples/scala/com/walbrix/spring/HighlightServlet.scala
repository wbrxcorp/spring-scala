package com.walbrix.spring

import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.IOUtils

/**
 * Created by shimarin on 15/03/13.
 */

case class Notation(content:String, title:Option[String] = None, description:Option[String] = None)

class HighlightServlet extends javax.servlet.http.HttpServlet with com.typesafe.scalalogging.slf4j.LazyLogging {
  private var template:Option[String] = None
  var contextRoot:String = _
  var version:String = _

  override def init():Unit = {
    template = Option(getInitParameter("template"))
    val servletContext = this.getServletContext
    contextRoot = servletContext.getContextPath
    version = ApplicationVersion(servletContext).getOrElse("UNKNOWN")
  }

  private def getNotation(path:String):Option[Notation] = {
    val mdPath = ReplaceFilenameSuffix(path, ".md")
    openStream(mdPath).map { is =>
      try {
        val md = ApplyVariables(IOUtils.toString(is, "UTF-8"), Map("contextRoot"->contextRoot, "version"->version))
        val (content, meta) = ExtractMetadataFromMarkdown(md)
        Notation(PegDown(content), meta.get("title"), meta.get("description"))
      }
      finally {
        is.close()
      }
    }
  }

  private def openStream(path:String):Option[java.io.InputStream] = {
    HighlightServlet.externalBasePath match {
      case Some(basepath) => try {Some(new java.io.FileInputStream(basepath + "/" + path))} catch { case x:java.io.FileNotFoundException=> None}
      case None => Option(getServletContext.getResourceAsStream(path))
    }
  }

  override def doGet(request:javax.servlet.http.HttpServletRequest, response:HttpServletResponse):Unit = {
    logger.debug("servletPath:%s, pathInfo:%s".format(request.getServletPath, request.getPathInfo))

    val resourcePath = request.getServletPath + Option(request.getPathInfo).getOrElse("")

    def sendNotFound:Unit = response.sendError(HttpServletResponse.SC_NOT_FOUND)

    val highlight = FilenameSuffixes(resourcePath).getOrElse {
      sendNotFound
      return
    }

    val is = openStream(resourcePath).getOrElse {
      sendNotFound
      return
    }
    val source = org.apache.commons.lang.StringEscapeUtils.escapeHtml(IOUtils.toString(is, "UTF-8"))
    template match {
      case Some(x) =>
        getNotation(resourcePath).foreach { notation =>
          request.setAttribute("content", notation.content)
          notation.title.foreach(request.setAttribute("title", _))
          notation.description.foreach(request.setAttribute("description", _))
        }
        request.setAttribute("language", highlight._1)
        request.setAttribute("highlight", highlight._2)
        request.setAttribute("path", resourcePath)
        request.setAttribute("source", source)
        request.getRequestDispatcher(x).forward(request, response)
      case None =>
        response.setContentType("text/html; charset=UTF-8")
        val writer = response.getWriter
        writer.write("<html>\n<head>\n<title>Source</title>\n")
        writer.write("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/styles/github.min.css\">\n<script src=\"http://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/highlight.min.js\"></script>\n<script>hljs.initHighlightingOnLoad();</script>")
        writer.write("</head>\n<body><pre><code>")
        writer.write(source)
        writer.write("</code></pre></body>\n</html>")
        writer.flush()
    }
  }
}

object HighlightServlet {
  var externalBasePath:Option[String] = None
}
