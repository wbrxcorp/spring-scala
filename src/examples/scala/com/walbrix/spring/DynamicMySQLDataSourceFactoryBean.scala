package com.walbrix.spring

import org.springframework.beans.FatalBeanException
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by shimarin on 15/04/17.
 */
class DynamicMySQLDataSourceFactoryBean extends com.typesafe.scalalogging.slf4j.LazyLogging {
  // HttpServletRequestを AutowiredにしておけばSpringがリクエストコンテキストから取り出して自動的にセットしてくれる
  @Autowired private var request:javax.servlet.http.HttpServletRequest = _

  def getObject: javax.sql.DataSource = {
    /* MySQLのホスト名、DB名、ユーザー名、パスワードをセッションから取り出す。それらの情報がセッションに保存
      されていない場合は例外を送出する */
    val session = Option(request.getSession(false)).getOrElse(throw new FatalBeanException("Could not obtain database connection info from session"))
    val serverName = Option(session.getAttribute("mysql.serverName").asInstanceOf[String]).getOrElse("localhost")
    val databaseName = Option(session.getAttribute("mysql.databaseName").asInstanceOf[String]).getOrElse(throw new FatalBeanException("MySQL database name is not specified"))
    val user = Option(session.getAttribute("mysql.user").asInstanceOf[String]).getOrElse(throw new FatalBeanException("MySQL user is not specified"))
    val password = Option(session.getAttribute("mysql.password").asInstanceOf[String])

    /* MySQLの JDBCドライバには MySQL専用の DataSourceクラスが同梱されているのでそれを使用する
      (DriverManagerDataSourceなどベンダ非依存のものを使用してももちろん構わない) */
    val dataSource = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource()
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
