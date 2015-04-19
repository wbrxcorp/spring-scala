package com.walbrix.spring

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

/**
 * Created by shimarin on 15/04/19.
 */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(Array("file:src/examples/webapp/WEB-INF/api-servlet.xml"))
@WebAppConfiguration("src/examples/webapp")
class WikipediaRequestHandlerTest {
  @Autowired private var target:WikipediaRequestHandler = _

  @Test def testHead():Unit = {
    println(target.parse("孫正義"))
  }
}
