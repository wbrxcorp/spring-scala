import java.io.File
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import javax.servlet.{Servlet, ServletContext}
import javax.servlet.http.{HttpSession, HttpServletResponse, HttpServletRequest}

import org.apache.commons.io.{FileUtils, IOUtils}
import org.eclipse.jetty.webapp.WebAppContext
import org.springframework.web.context.WebApplicationContext

import collection.JavaConversions._

import boot.MailTrapConfig
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.walbrix.jetty
import com.walbrix.spring.{ScalikeJdbcSupport, ObjectMapper, HighlightServlet}
import org.eclipse.jetty.server.{ServerConnector, Server}
import org.h2.jdbcx.JdbcDataSource
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.mock.web.{MockHttpServletResponse, MockHttpServletRequest}
import org.springframework.stereotype.Component
import org.springframework.web.context.request.ServletWebRequest
import scalikejdbc.{GlobalSettings, LoggingSQLAndTimeSettings}

/**
 * Created by shimarin on 15/01/30.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
case class Config(mailtrap:Option[MailTrapConfig] = None, scalikejdbc_log_config:Option[LoggingSQLAndTimeSettings] = None, port:Option[Int] = None)

object Main extends com.typesafe.scalalogging.slf4j.LazyLogging with ScalikeJdbcSupport with org.json4s.native.JsonMethods
{
  private var webappContext:WebAppContext = _
  private var server:Server = _
  private var _session:HttpSession = _

  def getServletContext:ServletContext = webappContext.getServletContext
  def getPort = server.getConnectors()(0).asInstanceOf[ServerConnector].getLocalPort

  def servlets:Map[String, Servlet] = {
    webappContext.getServletHandler.getServlets.map { servletHolder =>
      (servletHolder.getName, servletHolder.getServlet)
    }.toMap
  }

  def getServlet(name:String):Servlet = {
    webappContext.getServletHandler.getServlet(name).getServlet
  }

  def getWebApplicationContext(servletName:String):WebApplicationContext = {
    webappContext.getServletHandler.getServlet(servletName).getServlet.asInstanceOf[org.springframework.web.servlet.DispatcherServlet].getWebApplicationContext
  }

  /**
   * 現在のセッションオブジェクトを返す(セッションがない場合は作成)
   * @return
   */
  def session:HttpSession = {
    if (this._session == null) this._session = new org.springframework.mock.web.MockHttpSession(getServletContext)
    this._session
  }

  /**
   * JettyのセッションストアにあるセッションをIDで指定して横取りする
   * @param id
   * @return session
   */
  def session(id:String):HttpSession = {
    this._session = Option(webappContext.getSessionHandler.getSessionManager.getHttpSession(id)).getOrElse(throw new IllegalArgumentException("No such session"))
    this._session
  }

  def resetSession():Unit = this._session = null

  def request(path:String, method:String = "GET")(f:MockHttpServletRequest=>Unit):HttpServletRequest = {
    val request = new MockHttpServletRequest(getServletContext, method, path)
    request.setSession(session)
    f(request)
    request
  }

  def json(request:MockHttpServletRequest, content:String):Unit = {
    request.setContentType("application/json")
    request.setContent(content.getBytes("UTF-8"))
  }

  def getResourceAsByteArray(path:String):Array[Byte] = {
    IOUtils.toByteArray(this.getClass.getResourceAsStream(path))
  }

  def getWebAppResourceAsByteArray(path:String):Array[Byte] = {
    IOUtils.toByteArray(getServletContext.getResourceAsStream(path))
  }

  def exec(servletName:String, request:HttpServletRequest):MockHttpServletResponse = {
    val servlet = Option(getServlet(servletName)).getOrElse(throw new IllegalArgumentException("No such servlet"))
    val response = new MockHttpServletResponse()
    servlet.service(request, response)

    val (status, contentType) = (response.getStatus, response.getContentType)
    println("Status: %d".format(status))
    Option(contentType).foreach(ct => println("Content-type: %s".format(ct)))
    (status, contentType) match {
      case (200, x) if x.startsWith("text/") || x.startsWith("application/json") => println(response.getContentAsString)
      case _ => println("%d bytes".format(response.getContentAsByteArray.length))
    }
    response
  }

  def get(servletName:String, path:String, f:MockHttpServletRequest=>Unit = (r) => r.setCharacterEncoding("UTF-8")):MockHttpServletResponse = {
    exec(servletName, request(path, "GET")(f))
  }

  def post(servletName:String, path:String, contentType:String, content:Array[Byte], f:MockHttpServletRequest=>Unit = (r) => r.setCharacterEncoding("UTF-8")):MockHttpServletResponse = {
    exec(servletName, request(path, "POST") { req =>
      req.setContentType(contentType)
      req.setContent(content)
      f(req)
    })
  }

  def post(servletName:String, path:String, file:File):MockHttpServletResponse = {
    val contentType = Files.probeContentType(file.toPath)
    val content = FileUtils.readFileToByteArray(file)
    post(servletName, path, contentType, content)
  }

  def post(servletName:String, path:String, contentType:String, content:String):MockHttpServletResponse = {
    post(servletName, path, contentType, content.getBytes("UTF-8"))
  }

  def exec(servletName:String, path:String, method:String = "GET"):MockHttpServletResponse = {
    exec(servletName, request(path, method)(_.setCharacterEncoding("UTF-8")))
  }

  def getComponent(servletName:String):Map[String,AnyRef] = {
    getWebApplicationContext(servletName).getBeansWithAnnotation(classOf[Component]).toMap
  }

  def getComponent[T](servletName:String, clazz:Class[T]):T = {
    getWebApplicationContext(servletName).getBean[T](clazz)
  }

  def mockRequest(method:String, path:String):(MockHttpServletRequest, MockHttpServletResponse) = {
    val request = new MockHttpServletRequest(getServletContext, method, path)
    val response = new MockHttpServletResponse()

    org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(new ServletWebRequest(request, response))
    (request, response)
  }

  def run:Unit = {
    Option(this.server).foreach(x=>throw new IllegalStateException("Server is already running"))

    System.setProperty("org.apache.jasper.compiler.disablejsr199", "false")

    this.webappContext = jetty.createWebapp("src/examples/webapp", "")

    HighlightServlet.externalBasePath = Some(".")

    val localConfig = try {
      val f = new java.io.FileInputStream("local_config.json")
      try(ObjectMapper.readValue[Config](f)) finally(f.close)
    } catch { case ex:java.io.FileNotFoundException => Config() }

    logger.debug("Local config: " + localConfig.toString)

    // ScalikeJDBC log settings
    GlobalSettings.loggingSQLAndTime = localConfig.scalikejdbc_log_config.getOrElse(LoggingSQLAndTimeSettings(singleLineMode = true))

    val dataSource = new JdbcDataSource()
    dataSource.setURL("jdbc:h2:mem:dataSource;DB_CLOSE_DELAY=-1")
    dataSource.setUser("sa")
    new org.eclipse.jetty.plus.jndi.Resource("java:comp/env/jdbc/spring-scala", dataSource)
    this.setDataSource(dataSource)

    // setup MailSession
    boot.CreateMailTrapSession(localConfig.mailtrap.getOrElse(MailTrapConfig()))

    val (server, port) = jetty.run(Seq(this.webappContext), localConfig.port)
    this.server = server
    println("http://localhost:%d".format(port))
  }

  def chrome:Unit = {
    Option(this.server).map { server =>
      val driverPath = System.getProperty("os.name") match {
        case x if x.startsWith("Windows") => Some("bin/chromedriver.exe")
        case x if x.startsWith("Mac") => Some("bin/chromedriver")
        // if Linux, /usr/bin/chromedriver is expected
        case _ => None
      }
      driverPath.foreach(System.setProperty("webdriver.chrome.driver", _))
      val driver = new ChromeDriver()

      // カスタマイズするには https://sites.google.com/a/chromium.org/chromedriver/capabilities#TOC-Use-custom-profile-also-called-user-data-directory-

      driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
      val url = "http://localhost:%d/".format(getPort)
      driver.get(url)
    }.getOrElse(throw new IllegalStateException("Server is not running"))
  }

  def stop:Unit = {
    Option(this.server).map { server =>
      server.stop
      this.server = null
    }.getOrElse(throw new IllegalStateException("Server is not running"))
  }

  def main(args:Array[String]):Unit = {
    run
    chrome
  }
}
