package com.walbrix.spring

import org.junit.Test

/**
 * Created by shimarin on 15/04/06.
 */
class ApplyVariablesTest {
  @Test def testApply():Unit = {
    assert(ApplyVariables("Hello, ${name}!", Map("name"->"World")) == "Hello, World!")
  }
}
