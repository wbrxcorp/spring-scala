package db.migration

import com.walbrix.spring.ObjectMapper

/**
 * Created by shimarin on 15/05/03.
 */
class V9998__LocalConfig extends com.walbrix.flyway.ScalikeJdbcMigration with com.typesafe.scalalogging.slf4j.LazyLogging {

  private def openConfigFile(filename:String):Map[String,Any] = {
    try {
      val f = new java.io.FileInputStream(filename)
      (try(com.walbrix.spring.ObjectMapper.readValue[Map[String,Any]](f)) finally(f.close))
    } catch {
      case ex:java.io.FileNotFoundException =>
        logger.debug("Config file '%s' not found. skipping.".format(filename))
        Map()
    }
  }

  private def toString(value:Any):String = {
    value match {
      case x:String => x
      case x:Boolean => x.toString
      case x:Number => x.toString
      case x:Char => x.toString
      case x => ObjectMapper.underlying.writeValueAsString(x)
    }
  }

  override def migrate(implicit session: scalikejdbc.DBSession): Unit = {
    // <Environment name="spring-scala/configfile" value="/path/to/local_config.json" type="java.lang.String"/>
    (com.walbrix.spring.JNDI("java:comp/env/spring-scala/configfile").map(openConfigFile(_)).getOrElse(Map()) ++ openConfigFile("local_config.json")).foreach { case (configKey, configValue) =>
      sql"merge into system_configs(config_key,config_value) KEY(config_key) values(${configKey},${toString(configValue)})".update.apply()
    }
  }
}
