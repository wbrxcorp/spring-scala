import java.util.Properties
import java.util.concurrent.TimeUnit

import com.walbrix.jetty
import org.h2.jdbcx.JdbcDataSource
import org.openqa.selenium.chrome.ChromeDriver

/**
 * Created by shimarin on 15/01/30.
 */

object Main {
  def main(args:Array[String]):Unit = {
    //System.setProperty("java.util.logging.config.file", "logging.properties")

    val root = jetty.createWebapp("src/main/webapp", "")

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

    val (server, port) = jetty.run(Seq(root))
    println("http://localhost:%d".format(port))

    System.setProperty("webdriver.chrome.driver", System.getProperty("os.name") match {
      case x if x.startsWith("Windows") => "src/test/chromedriver.exe"
      case x if x.startsWith("Mac") => "src/test/chromedriver-mac"
      case x if x.startsWith("Linux") => "src/test/chromedriver-linux"
      case _ => "src/test/chromedriver"
    })
    val driver = new ChromeDriver()
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    val url = "http://localhost:%d/".format(port)
    driver.get(url)
  }
}
