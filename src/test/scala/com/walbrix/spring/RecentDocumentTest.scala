package com.walbrix.spring

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

/**
 * Created by shimarin on 15/04/18.
 */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration
@WebAppConfiguration("src/examples/webapp")
class RecentDocumentTest extends com.typesafe.scalalogging.slf4j.LazyLogging {
  @Autowired private var servletContext:javax.servlet.ServletContext = _

  @Test def testGetRecentDocuments():Unit = {
    GetRecentDocuments(servletContext, "/").foreach { doc =>
      println(doc)
    }
  }
}
