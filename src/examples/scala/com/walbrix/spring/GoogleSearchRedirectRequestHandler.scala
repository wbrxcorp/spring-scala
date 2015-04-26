package com.walbrix.spring

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}

/**
 * Created by shimarin on 15/04/26.
 */
@Controller
@RequestMapping(value=Array("search"))
class GoogleSearchRedirectRequestHandler {
  @RequestMapping(value=Array(""))
  def get(@RequestParam q:String):String = {
    return "redirect:https://www.google.co.jp/search?q=site%3Awww.walbrix.com%2Fspring-scala%2F+" + java.net.URLEncoder.encode(q, "UTF-8");
  }
}
