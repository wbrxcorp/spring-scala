package com.walbrix.spring

import java.sql.SQLException
import javax.servlet.http.HttpSession

import com.typesafe.scalalogging.slf4j.LazyLogging
import com.walbrix.spring.mvc.{Fail, Success, Result}
import org.springframework.beans.factory.BeanCreationException
import org.springframework.jdbc.CannotGetJdbcConnectionException
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation._

/**
 * Created by shimarin on 15/04/17.
 */
case class DatabaseInfo(serverName:String, databaseName:String, user:String, password:Option[String])

@RestController
@RequestMapping(value=Array(""))
class DynamicMySQLDataSourceRequestHandler extends ScalikeJdbcSupport with LazyLogging {

  @ExceptionHandler(value=Array(classOf[BeanCreationException], classOf[CannotGetJdbcConnectionException], classOf[SQLException]))
  def exception(ex:Exception):Result[String] = {
    logger.error("Exception", ex)
    Fail(ex.getMessage)
  }

  @RequestMapping(value=Array(""), method=Array(RequestMethod.POST))
  def save(session:HttpSession, @RequestBody info:DatabaseInfo):Result[AnyRef] = {
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
    single(sql"select 1".toMap())
    Success(list(sql"select 1+1 as oneplusone"))
  }
}
