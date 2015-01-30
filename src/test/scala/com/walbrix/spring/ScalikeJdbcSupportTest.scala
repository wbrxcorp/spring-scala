package com.walbrix.spring

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import scalikejdbc._

/**
 * Created by shimarin on 14/11/16.
 */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration
@Transactional
class ScalikeJdbcSupportTest extends ScalikeJdbcSupport {
  @Test def testExecute():Unit = {
    assert(execute(sql"select * from users"))
  }
}
