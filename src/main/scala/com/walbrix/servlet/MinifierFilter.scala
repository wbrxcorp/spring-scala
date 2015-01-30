package com.walbrix.servlet

import java.io.{FileNotFoundException, InputStream, InputStreamReader}
import javax.servlet._
import javax.servlet.http.HttpServletRequest
import com.yahoo.platform.yui.compressor.{CssCompressor, JavaScriptCompressor}

import org.apache.commons.logging.{Log, LogFactory}

import scala.io.Source

/**
 * Created by shimarin on 14/11/03.
 */
class MinifierFilter extends Filter {
  private var context:ServletContext = _
  private var lineBreak:Int = -1
  private var noMunge:Boolean = false
  private var preserveSemi:Boolean = false
  private var disableOptimizations:Boolean = false
  val log:Log = LogFactory.getLog(this.getClass)

  override def init(config: FilterConfig): Unit = {
    log.debug("Initializing MinifierFilter")

    this.context = config.getServletContext()

    this.lineBreak = Option(config.getInitParameter("lineBreak")).map(_.toInt).getOrElse(this.lineBreak)
    this.noMunge = Option(config.getInitParameter("noMunge")).map(_.toBoolean).getOrElse(this.noMunge)
    this.preserveSemi = Option(config.getInitParameter("preserveSemi")).map(_.toBoolean).getOrElse(this.preserveSemi)
    this.disableOptimizations = Option(config.getInitParameter("disableOptimizations")).map(_.toBoolean).getOrElse(this.disableOptimizations)
  }

  private def findResourceToCompress(path:String):Seq[InputStream] = {
    if (context.getResource(path) != null) return Seq.empty
    val regex = """\.min.(css|js)$""".r
    if (regex.findFirstIn(path) == None) return Seq.empty // min.(css|js) ではない

    Option(context.getResourceAsStream(path + ".lst")).map { lstfile =>
      try {
        val source = Source.fromInputStream(lstfile, "UTF-8")
        try {
          val lines = source.getLines.toList
          lines.map(_.trim).filter { line =>
            !line.isEmpty && !line.startsWith("#")
          }.map { line =>
            Option(context.getResourceAsStream(line)).getOrElse(throw new FileNotFoundException(line))
          }
        }
        finally {
          source.close()
        }
      }
      finally {
        lstfile.close()
      }
    }.getOrElse {
      Option(context.getResourceAsStream(regex.replaceFirstIn(path, """.$1"""))).map { resource =>
        Seq(resource)
      }.getOrElse(Seq.empty)
    }
  }

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    val path = request.asInstanceOf[HttpServletRequest].getServletPath
    log.debug("processing %s".format(path))
    val resources = findResourceToCompress(path)
    if (resources.isEmpty) {
      log.debug("%s - nothing to be compressed".format(path))
      chain.doFilter(request, response)
      return
    }
    //else
    response.setCharacterEncoding("UTF-8")
    val out = response.getWriter
    resources.map { resource =>
      try {
        val in = new InputStreamReader(resource, "UTF-8")
        try {
          if (path.endsWith(".js")) {
            response.setContentType("application/javascript")
            val compressor = new JavaScriptCompressor(in, null);
            compressor.compress(out, this.lineBreak, !this.noMunge, false, this.preserveSemi, this.disableOptimizations);
          } else if (path.endsWith(".css")) {
            response.setContentType("text/css")
            val compressor = new CssCompressor(in);
            compressor.compress(out, this.lineBreak);
          } else {
            assert(false) // can not be here
          }
        }
        finally {
          in.close()
        }
        out.flush()
      }
      finally {
        resource.close()
      }
    }
  }

  override def destroy(): Unit = {}
}
