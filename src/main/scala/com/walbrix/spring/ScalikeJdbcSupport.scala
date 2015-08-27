package com.walbrix.spring

import java.sql.{SQLException, Connection}
import javax.sql.DataSource

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.jdbc.support.{SQLErrorCodeSQLExceptionTranslator, SQLExceptionTranslator}
import scalikejdbc.GeneralizedTypeConstraintsForWithExtractor._
import scalikejdbc._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.datasource.DataSourceUtils

import scala.collection.generic.CanBuildFrom

/**
 * Created by shimarin on 14/11/02.
 */

class TransactionAwareDBSession(private val dataSource:DataSource,
                                override val isReadOnly:Boolean = false,
                                override val connectionAttributes:DBConnectionAttributes = DBConnectionAttributes()) extends scalikejdbc.DBSession with LazyLogging {
  override val conn:Connection = {
    val conn = DataSourceUtils.getConnection(dataSource)
    logger.debug("DataSourceUtils.getConnection = %s".format(conn.toString))
    conn
  }
  override def close(): Unit = {
    util.control.Exception.ignoring(classOf[Throwable]) {
      logger.debug("DataSourceUtils.releaseConnection(%s)".format(conn.toString))
      DataSourceUtils.releaseConnection(conn, dataSource)
    }
  }
}

trait ScalikeJdbcSupport extends SQLInterpolation {
  private var dataSource:DataSource = _

  @Autowired def setDataSource(dataSource:DataSource):Unit = {
    this.dataSource = dataSource
  }

  def scalikeJdbcSession[T](task:String,sql:String,f:DBSession=>T):T = {
    try {
      scalikejdbc.using(new TransactionAwareDBSession(dataSource)) { session =>
        f(session)
      }
    }
    catch {
      // SQLErrorCodeSQLExceptionTranslatorはsetDataSource時にnewしたほうが省資源なのだが、それだと
      // DataSourceがSingleton以外のスコープで定義されているときに死ぬので都度生成とした。
      case e:SQLException => throw new SQLErrorCodeSQLExceptionTranslator(dataSource).translate("", "", e)
    }
  }

  def apply[C[_]](sql:SQLBatch)(implicit cbf: CanBuildFrom[Nothing, Int, C[Int]]):C[Int] = {
    scalikeJdbcSession("batch", sql.statement, sql()(_, cbf))
  }

  def apply(sql:SQLExecution):Boolean = {
    scalikeJdbcSession("execute", sql.statement, sql()(_))
  }

  def execute[A](sql:SQL[A, NoExtractor]):Boolean = {
    apply(sql.execute())
  }

  def apply(sql:SQLUpdate):Int = {
    scalikeJdbcSession("update", sql.statement, sql()(_))
  }

  def update[A](sql:SQL[A, NoExtractor]):Int = {
    apply(sql.update())
  }

  def apply[A,E <: WithExtractor](sql:SQLToTraversable[A, E])(implicit hasExtractor: SQL[A, E] =:= SQL[A, HasExtractor]):Traversable[A] = {
    scalikeJdbcSession("traverse", sql.statement, sql()(_, NoConnectionPoolContext,hasExtractor))
  }

  def apply[A,E <: WithExtractor](sql:SQLToList[A, E])(implicit hasExtractor: SQL[A, E] =:= SQL[A, HasExtractor]):List[A] = {
    scalikeJdbcSession("list", sql.statement, sql()(_, NoConnectionPoolContext,hasExtractor))
  }

  def list[A,E <: WithExtractor](sql:SQL[A,E])(implicit hasExtractor: SQL[A, E] =:= SQL[A, HasExtractor]):List[A] = {
    apply(sql.list())
  }

  def apply[A,E <: WithExtractor](sql:SQLToOption[A, E])(implicit hasExtractor: SQL[A, E] =:= SQL[A, HasExtractor]):Option[A] = {
    scalikeJdbcSession("single", sql.statement, sql()(_, NoConnectionPoolContext,hasExtractor))
  }

  def single[A,E <: WithExtractor](sql:SQL[A,E])(implicit hasExtractor: SQL[A, E] =:= SQL[A, HasExtractor]):Option[A] = {
    apply(sql.single())
  }

  def single[A](sql:SQL[A, NoExtractor]):Option[Map[String,Any]] = single(sql.map(_.toMap()))
  def list[A](sql:SQL[A, NoExtractor]):List[Map[String,Any]] = list(sql.map(_.toMap()))

  def int[A](sql:SQL[A, NoExtractor]):Option[Int] = single(sql.map(_.int(1)))
  def string[A](sql:SQL[A, NoExtractor]):Option[String] = single(sql.map(_.string(1)))
  def boolean[A](sql:SQL[A, NoExtractor]):Option[Boolean] = single(sql.map(_.boolean(1)))
  def long[A](sql:SQL[A, NoExtractor]):Option[Long] = single(sql.map(_.long(1)))

  def exists(condition:SQLSyntax):Boolean = {
    boolean(sql"select exists(select 1 from ${condition})").get
  }
}
