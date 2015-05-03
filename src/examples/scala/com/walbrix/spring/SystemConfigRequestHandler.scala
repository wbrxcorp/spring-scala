package com.walbrix.spring

import java.io.FileNotFoundException
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
 * Created by shimarin on 15/05/03.
 */
@RestController
@RequestMapping(Array("system_config"))
class SystemConfigRequestHandler extends ScalikeJdbcSupport with MustBeLocalhost {

  @ExceptionHandler(Array(classOf[FileNotFoundException]))
  def notfound(ex:Exception, response:HttpServletResponse):Unit = {
    response.sendError(HttpServletResponse.SC_NOT_FOUND)
  }

  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  def get():Map[String,String] = {
    mustBeLocalhost
    list(sql"select config_key,config_value from system_configs".map(row => row.string(1)->row.string(2))).toMap
  }

  @RequestMapping(value=Array(""), method=Array(RequestMethod.POST))
  def put(@RequestBody body:Map[String,Any]):{ val success:Boolean; val info:Option[Any] } = {
    mustBeLocalhost
    val cnt = body.foldLeft(0) { case (cnt, (configKey, configValue)) =>
      cnt + update(sql"merge into system_configs(config_key,config_value) KEY(config_key) values(${configKey},${configValue.toString})")
    }
    new { val success = true; val info = Some(cnt) }
  }

  @RequestMapping(value=Array("{configKey}"), method=Array(RequestMethod.DELETE))
  def delete(@PathVariable configKey:String):{ val success:Boolean; val info:Option[Any]} = {
    mustBeLocalhost
    if (update(sql"delete from system_configs where config_key=${configKey}") == 0) throw new FileNotFoundException(configKey)
    new { val success = true; val info = None}
  }
}
