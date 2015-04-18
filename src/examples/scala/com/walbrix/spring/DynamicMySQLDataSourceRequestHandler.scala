package com.walbrix.spring

import com.walbrix.spring.mvc.{Fail, Success, Result}
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation._

/**
 * Created by shimarin on 15/04/17.
 */
case class DatabaseInfo(serverName:String, databaseName:String, user:String, password:Option[String])

@RestController
@RequestMapping(value=Array(""))
class DynamicMySQLDataSourceRequestHandler extends ScalikeJdbcSupport with com.typesafe.scalalogging.slf4j.LazyLogging {

  @ExceptionHandler(value=Array(classOf[org.springframework.beans.factory.BeanCreationException], classOf[org.springframework.jdbc.CannotGetJdbcConnectionException], classOf[java.sql.SQLException]))
  def exception(ex:Exception):Result[String] = {
    logger.error("Exception", ex)
    Fail(ex.getMessage)
  }

  @RequestMapping(value=Array(""), method=Array(RequestMethod.POST))
  def save(session:javax.servlet.http.HttpSession, @RequestBody info:DatabaseInfo):Result[AnyRef] = {
    session.setAttribute("mysql.serverName", info.serverName)
    session.setAttribute("mysql.databaseName", info.databaseName)
    session.setAttribute("mysql.user", info.user)
    session.removeAttribute("mysql.password")
    info.password.foreach(session.setAttribute("mysql.password", _))
    Success()
  }

  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  @Transactional
  def makeQuery():Result[Seq[Map[String,Any]]] = {
    /* @Transactionalアノテーションが効いているかどうか確認するため、わざと複数回クエリを発行している
       (ScalikeJdbcSupportのデバッグログで同じDBSessionオブジェクトが使いまわされていることがわかる) */
    single(sql"select 1".toMap())
    Success(list(sql"select 1+1 as oneplusone"))
  }
}
