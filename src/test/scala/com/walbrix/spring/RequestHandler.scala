package com.walbrix.spring

import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.{RequestBody, ResponseBody, RequestMethod, RequestMapping}

/**
 * Created by shimarin on 15/02/01.
 */

trait Base {
  case class Result(success:Boolean,info:Option[Any] = None)

  def test1(body:Map[String,Option[Any]]):Result

  @RequestMapping(value=Array("test1"), method = Array(RequestMethod.POST))
  @ResponseBody
  def _test1(@RequestBody body:Map[String,Option[Any]]):Result = test1(body)
}

@Controller
@Transactional
@RequestMapping(Array(""))
class RequestHandler extends Base {
  override def test1(body:Map[String,Option[Any]]):Result = {
    println(body)
    Result(true)
  }
}
