import java.util.Properties
import java.util.concurrent.TimeUnit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.walbrix.jetty
import com.walbrix.spring.{ObjectMapper, HighlightServlet}
import org.eclipse.jetty.plus.jndi.EnvEntry
import org.h2.jdbcx.JdbcDataSource
import org.openqa.selenium.chrome.ChromeDriver
import scalikejdbc.{GlobalSettings, LoggingSQLAndTimeSettings}

/**
 * Created by shimarin on 15/01/30.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
case class Config(mailtrap:Option[MailTrapConfig] = None, port:Option[Int] = None)
case class MailTrapConfig(id:String,password:String)

object Main extends com.typesafe.scalalogging.slf4j.LazyLogging {
  def main(args:Array[String]):Unit = {
    // ScalikeJDBC log settings
    GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
      enabled = true,
      singleLineMode = true,
      logLevel = 'DEBUG
    )

    System.setProperty("org.apache.jasper.compiler.disablejsr199", "false")

    val root = jetty.createWebapp("src/examples/webapp", "")

    HighlightServlet.externalBasePath = Some(".")

    val localConfig = try {
      val f = new java.io.FileInputStream("local_config.json")
      try(ObjectMapper.readValue[Config](f)) finally(f.close)
    } catch { case ex:java.io.FileNotFoundException => Config() }

    logger.debug("Local config: " + localConfig.toString)

    // setup DataSource
    val dataSource = new JdbcDataSource()
    dataSource.setURL("jdbc:h2:mem:spring-scala;DB_CLOSE_DELAY=-1")
    new org.eclipse.jetty.plus.jndi.Resource("java:comp/env/jdbc/spring-scala", dataSource)

    // setup MailSession
    val mailref = new org.eclipse.jetty.jndi.factories.MailSessionReference()
    mailref.setUser(localConfig.mailtrap.map(_.id).getOrElse("MAILTRAP_ID"))
    mailref.setPassword(localConfig.mailtrap.map(_.password).getOrElse("MAILTRAP_PASSWORD"))
    val properties = new Properties()
    properties.setProperty("mail.smtp.auth","true")
    properties.setProperty("mail.smtp.host", "mailtrap.io")
    properties.setProperty("mail.smtp.port", "465")
    mailref.setProperties(properties)
    new org.eclipse.jetty.plus.jndi.Resource("java:comp/env/mail/Session", mailref)

    val (server, port) = jetty.run(Seq(root), localConfig.port)
    println("http://localhost:%d".format(port))

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
    val url = "http://localhost:%d/".format(port)
    driver.get(url)
  }
}
