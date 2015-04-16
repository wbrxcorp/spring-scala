import java.net.URLClassLoader

import com.walbrix.spring.HighlightServlet
import org.eclipse.jetty.server.{ServerConnector, Server}
import org.eclipse.jetty.webapp.WebAppContext
import org.junit._
import org.scalatest.selenium.Chrome
import scalikejdbc.{LoggingSQLAndTimeSettings, GlobalSettings}

/**
 * Created by shimarin on 15/04/17.
 */
class E2ETest extends Chrome {
  @Before def tearUp:Unit = {
    go to "http://localhost:%d/".format(E2ETest.getPort())
  }

  @After def tearDown:Unit = {
    close
  }

  @Test def foo():Unit = {

  }

  @Ignore @Test def justStart:Unit = {
    E2ETest.join()
  }
}

object E2ETest {
  private var server:Server = _

  // ScalikeJDBC log settings
  GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'DEBUG
  )

  val driverPath = System.getProperty("os.name") match {
    case x if x.startsWith("Windows") => Some("src/test/chromedriver.exe")
    case x if x.startsWith("Mac") => Some("src/test/chromedriver")
    // if Linux, /usr/bin/chromedriver is expected
    case _ => None
  }
  driverPath.foreach(System.setProperty("webdriver.chrome.driver", _))

  def join():Unit = server.join()
  def getPort():Int = server.getConnectors()(0).asInstanceOf[ServerConnector].getLocalPort()

  @BeforeClass def before():Unit = {
    server = new Server(0)
    val webapp = new WebAppContext()

    webapp.setResourceBase("src/examples/webapp")
    webapp.setContextPath("/")
    webapp.setClassLoader(new URLClassLoader(new Array[java.net.URL](0), this.getClass().getClassLoader()))
    webapp.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false")
    server.setHandler(webapp)

    HighlightServlet.externalBasePath = Some(".")

    server.start()
  }

  @AfterClass def after():Unit = {
    server.stop()
    server.destroy()
  }

}
