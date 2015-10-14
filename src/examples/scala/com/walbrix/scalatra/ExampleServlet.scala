package com.walbrix.scalatra

import scalikejdbc.ThreadLocalDB
import org.scalatra.Ok

/**
 * Created by shimarin on 15/10/14.
 */
class ExampleServlet extends org.scalatra.ScalatraServlet with scalikejdbc.SQLInterpolation {
  private implicit val formats = org.json4s.DefaultFormats
  private val dataSource = new javax.naming.InitialContext().lookup("java:comp/env/jdbc/spring-scala").asInstanceOf[javax.sql.DataSource]

  def localTx[T](f:scalikejdbc.DBSession=>T) = ThreadLocalDB.load.localTx(f(_))

  before() {
    ThreadLocalDB.create(dataSource.getConnection)
    println("Before")
  }

  after() {
    try { ThreadLocalDB.load.close() } catch { case e: Exception => } // DB.closeは物理コネクションをクローズする
    println("After")
  }

  get("/") {
    contentType = "application/json"
    localTx { implicit session =>
      val two = sql"select 1+1".map(_.int(1)).single().apply()
      Ok(org.json4s.native.Serialization.write(Map("abc"->two, "hoge"->"ふが")))
    }
  }
}
