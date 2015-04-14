import java.util.Properties
import java.util.concurrent.TimeUnit

import com.walbrix.jetty
import com.walbrix.spring.HighlightServlet
import org.h2.jdbcx.JdbcDataSource
import org.openqa.selenium.chrome.ChromeDriver
import scalikejdbc.{GlobalSettings, LoggingSQLAndTimeSettings}

/**
 * Created by shimarin on 15/01/30.
 */

object Main {
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

    // setup DataSource
    val dataSource = new JdbcDataSource()
    dataSource.setURL("jdbc:h2:mem:spring-scala;DB_CLOSE_DELAY=-1")
    new org.eclipse.jetty.plus.jndi.Resource("java:comp/env/jdbc/spring-scala", dataSource)

    // setup MailSession
    val mailref = new org.eclipse.jetty.jndi.factories.MailSessionReference()
    mailref.setUser("MAILTRAP_ID")
    mailref.setPassword("MAILTRAP_PASSWORD")
    val properties = new Properties()
    properties.setProperty("mail.smtp.auth","true")
    properties.setProperty("mail.smtp.host", "mailtrap.io")
    properties.setProperty("mail.smtp.port", "465")
    mailref.setProperties(properties)
    new org.eclipse.jetty.plus.jndi.Resource("java:comp/env/mail/Session", mailref)

    val (server, port) = jetty.run(Seq(root), Some(41829))
    println("http://localhost:%d".format(port))

    val driverPath = System.getProperty("os.name") match {
      case x if x.startsWith("Windows") => Some("src/test/chromedriver.exe")
      case x if x.startsWith("Mac") => Some("src/test/chromedriver")
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
