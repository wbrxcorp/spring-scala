package com.walbrix.spring.googleapi

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RequestParam, ResponseBody}

/**
 * Created by shimarin on 15/04/01.
 */
@Controller
@RequestMapping(Array("google"))
class GoogleApiRequestHandler {
  @Autowired private var googleApi:GoogleApiClient = _

  @RequestMapping(value=Array("hello"), method=Array(RequestMethod.GET))
  @ResponseBody
  def hello(@RequestParam accessToken:String):UserInfo = {
    googleApi.getUserInfo(accessToken)
  }

}
