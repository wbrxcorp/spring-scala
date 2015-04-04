package com.walbrix.spring.googleapi

import com.walbrix.spring.GoogleApiRequestHandler
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 * Created by shimarin on 15/04/01.
 */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration
class GoogleApiRequestHandlerTest {
  @Autowired private var target:GoogleApiRequestHandler = _
  @Test def test():Unit = {
    println(target.hello("ya29.xxxxxxxxxxxNobySuIhje0vhgHw"))
  }
}
