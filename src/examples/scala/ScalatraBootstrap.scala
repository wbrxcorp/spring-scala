import com.walbrix.scalatra.ExampleServlet

/**
 * Created by shimarin on 15/10/14.
 */
class ScalatraBootstrap extends org.scalatra.LifeCycle {
  override def init(context: javax.servlet.ServletContext) {
    context.mount(new ExampleServlet, "/scalatra/*")
  }
}
