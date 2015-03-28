package com.walbrix.spring

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 * Created by shimarin on 2015/03/28.
 */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration
class AmazonServiceTest extends LazyLogging {
  @Autowired private var amazonService:AmazonService = _
  @Test def testQuery():Unit = {
    val response = amazonService.query("ItemSearch", Map(
      "Keywords"->"田村ゆかり",
      "SearchIndex"->"All"))

    (response \\ "Item").foreach { item => logger.info(item.toString()) }
  }
}
