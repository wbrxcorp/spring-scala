package com.walbrix.spring

import java.io.{StringWriter, FileNotFoundException, FileInputStream}
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringEscapeUtils
import org.apache.velocity.VelocityContext
import org.pegdown.{Extensions, PegDownProcessor}

import scala.beans.BeanProperty

/**
 * Created by shimarin on 15/03/13.
 */

case class Notation(@BeanProperty content:String, title:Option[String] = None, description:Option[String] = None)

class HighlightServlet extends HttpServlet with LazyLogging {
  private var template:Option[String] = None
  val metadataRegex ="""^(.+:.+\n)+\n""".r

  override def init():Unit = {
    template = Option(getInitParameter("template"))
  }

  private def parseMetadata(md:String):(String,Map[String,String]) = {
      metadataRegex.findFirstIn(md) match {
      case Some(metadata) =>
        val metamap = """(?m)^(.+:.+)$""".r.findAllIn(metadata).map { single =>
          val splitted = single.split(":")
          (splitted(0).trim, splitted(1).trim)
        }.toMap
        //logger.debug(metadataRegex.split(md).toSeq.toString)
        ((metadataRegex.split(md) ++ Array("","")).apply(1), metamap)
      case None => (md, Map())
    }
  }

  private def getNotation(path:String):Option[Notation] = {
    val mdPath = path.split("\\.(?=[^\\.]+$)")(0) + ".md"
    openStream(mdPath).map { is =>
      try {
        val contextRoot = this.getServletContext.getContextPath
        val md = ApplyVariables(IOUtils.toString(is, "UTF-8"), Map("contextRoot"->contextRoot))
        val (content, meta) = parseMetadata(md)
        Notation(PegDown(content), meta.get("title"), meta.get("description"))
      }
      finally {
        is.close()
      }
    }
  }

  private def openStream(path:String):Option[java.io.InputStream] = {
    HighlightServlet.externalBasePath match {
      case Some(basepath) => try {Some(new FileInputStream(basepath + "/" + path))} catch { case x:FileNotFoundException=> None}
      case None => Option(getServletContext.getResourceAsStream(path))
    }
  }

  override def doGet(request:HttpServletRequest, response:HttpServletResponse):Unit = {
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
    val source = StringEscapeUtils.escapeHtml(IOUtils.toString(is, "UTF-8"))
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
