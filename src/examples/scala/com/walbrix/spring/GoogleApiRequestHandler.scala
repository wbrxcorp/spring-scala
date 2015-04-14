package com.walbrix.spring

import _root_.com.walbrix.spring.googleapi.{GoogleApiClient, UserInfo}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
 * Created by shimarin on 15/04/01.
 */
@RestController
@RequestMapping(Array("google"))
class GoogleApiRequestHandler {
  @Autowired private var googleApi:GoogleApiClient = _

  @RequestMapping(value=Array("hello"), method=Array(RequestMethod.GET))
  def hello(@RequestParam accessToken:String):UserInfo = {
    googleApi.getUserInfo(accessToken)
  }

}
