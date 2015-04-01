package com.walbrix.spring

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, ResponseBody, RequestMethod, RequestMapping}

import scala.concurrent.duration.Duration
import scala.concurrent.Await

import dispatch.Defaults._

/**
 * Created by shimarin on 15/04/01.
 */
@Controller
@RequestMapping(Array("google"))
class GoogleApiRequestHandler {
  @Autowired private var objectMapper:ObjectMapper = _

  @RequestMapping(value=Array("hello"), method=Array(RequestMethod.GET))
  @ResponseBody
  def hello(@RequestParam accessToken:String):JsonNode = {
    val svc = dispatch.url("https://www.googleapis.com/oauth2/v2/userinfo") <:< Map("Authorization" -> "Bearer %s".format(accessToken))
    val userinfo = dispatch.Http(svc OK dispatch.as.Bytes)

    objectMapper.readTree(Await.result(userinfo, Duration.Inf))
  }

}
