import javax.servlet.ServletContext

import com.walbrix.scalatra.{EntityWithImageServlet, ExampleServlet}

/**
 * Created by shimarin on 15/10/14.
 */
class ScalatraBootstrap extends org.scalatra.LifeCycle {
  private def autowire[T](target:T, context:ServletContext):T = {
    org.springframework.web.context.support.SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(target, context)
    target
  }

  override def init(context: ServletContext) {
    context.mount(autowire(new ExampleServlet, context), "/scalatra/*")
    context.mount(autowire(new EntityWithImageServlet, context), "/entitywithimage/*")
  }
}
