package com.walbrix.spring

import scala.reflect.ClassTag

/**
 * Created by shimarin on 15/05/03.
 */
trait SystemConfigSupport extends ScalikeJdbcSupport {
  def getSystemConfigString(configKey:String):Option[String] = single(sql"select config_value from system_configs where config_key=${configKey}".map(_.string(1)))

  def getSystemConfig[T:ClassTag](configKey:String):Option[T] = {
    getSystemConfigString(configKey).map(value => ObjectMapper.readValue[T](value))
  }
}
