package com.walbrix.spring

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

/**
 * Created by shimarin on 15/04/18.
 */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration
@WebAppConfiguration("src/examples/webapp")
class RecentDocumentTest extends RecentDocument with com.typesafe.scalalogging.slf4j.LazyLogging {

  @Test def testGetRecentDocuments():Unit = {
    getRecentDocuments("/").foreach { doc =>
      println(doc)
    }
  }
}
