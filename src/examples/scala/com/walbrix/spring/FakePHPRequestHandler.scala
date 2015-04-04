package com.walbrix.spring

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, ResponseBody}

/**
 * Created by shimarin on 15/03/28.
 */
@Controller
@RequestMapping(Array(""))
class FakePHPRequestHandler {
  @RequestMapping(value=Array("hello"), method=Array(RequestMethod.GET))
  @ResponseBody
  def hello():Tuple1[String] = {
    Tuple1("Hello, World!")
  }
}
