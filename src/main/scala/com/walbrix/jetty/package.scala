package com.walbrix

import org.eclipse.jetty.server.{ServerConnector, Server}
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.webapp.WebAppContext

/**
 * Created by shimarin on 14/11/17.
 */
package object jetty {
  def createWebapp(resourceBase:String, contextPath:String):WebAppContext = {
    val webapp = new WebAppContext()

    webapp.setResourceBase(resourceBase)
    webapp.setContextPath(contextPath)
    webapp.setClassLoader(this.getClass().getClassLoader())
    webapp.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false")
    webapp
  }

  def run(webapps:Seq[WebAppContext], port:Option[Int]=None):(Server,Int) = {
    val server = new Server(port.getOrElse(0))
    val handlers = new HandlerList()
    webapps.foreach(handlers.addHandler(_))
    server.setHandler(handlers)
    server.start()
    (server, server.getConnectors()(0).asInstanceOf[ServerConnector].getLocalPort())
  }

}
