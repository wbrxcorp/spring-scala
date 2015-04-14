package com.walbrix.spring

import org.springframework.web.bind.annotation.{RestController, RequestMapping, RequestMethod}

/**
 * Created by shimarin on 15/03/28.
 */
@RestController
@RequestMapping(Array(""))
class FakePHPRequestHandler {
  @RequestMapping(value=Array("hello"), method=Array(RequestMethod.GET))
  def hello():Tuple1[String] = {
    Tuple1("Hello, World!")
  }
}
