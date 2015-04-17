package com.walbrix.spring

import javax.servlet.http.HttpServletRequest
import javax.sql.DataSource

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.beans.FatalBeanException
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.annotation.Autowired


/**
 * Created by shimarin on 15/04/17.
 */
class DynamicMySQLDataSourceFactoryBean extends LazyLogging {
  @Autowired private var request:HttpServletRequest = _

  def getObject: DataSource = {
    val session = Option(request.getSession(false)).getOrElse(throw new FatalBeanException("Could not obtain database connection info from session"))
    val serverName = Option(session.getAttribute("mysql.serverName").asInstanceOf[String]).getOrElse("localhost")
    val databaseName = Option(session.getAttribute("mysql.databaseName").asInstanceOf[String]).getOrElse(throw new FatalBeanException("MySQL database name is not specified"))
    val user = Option(session.getAttribute("mysql.user").asInstanceOf[String]).getOrElse(throw new FatalBeanException("MySQL user is not specified"))
    val password = Option(session.getAttribute("mysql.password").asInstanceOf[String])

    val dataSource = new MysqlDataSource()
    dataSource.setServerName(serverName)
    dataSource.setDatabaseName(databaseName)
    dataSource.setUseUnicode(true)
    dataSource.setCharacterEncoding("UTF8")
    dataSource.setZeroDateTimeBehavior("convertToNull")
    dataSource.setUser(user)
    password.foreach(dataSource.setPassword(_))
    logger.debug("DynamicMySQLDataSourceFactoryBean.getObject = %s".format(dataSource.toString))
    dataSource
  }
}
