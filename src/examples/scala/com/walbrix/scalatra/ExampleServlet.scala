package com.walbrix.scalatra

import com.typesafe.scalalogging.slf4j.LazyLogging
import com.walbrix.spring.{TransactionSupport, ScalikeJdbcSupport}
import org.json4s.JObject
import org.springframework.web.context.support.SpringBeanAutowiringSupport
import org.scalatra.Ok

import scala.util.Try

/**
 * Created by shimarin on 15/10/14.
 */
case class Hiya(abc:Option[Int], hoge:String)

class ExampleServlet extends org.scalatra.ScalatraServlet with org.scalatra.json.NativeJsonSupport with ScalikeJdbcSupport with TransactionSupport with LazyLogging {
  override protected implicit def jsonFormats: org.json4s.Formats = org.json4s.DefaultFormats

  override def init(config:javax.servlet.ServletConfig) {
    super.init(config)
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext)
  }

  before() {
    println("Before")
  }

  after() {
    println("After")
  }

  get("/") {
    contentType = "application/json"
    tx {
      val two = single(sql"select 1+1".map(_.int(1)).single())
      val three = single(sql"select 1+2".map(_.int(1)).single())
      Ok(Hiya(two, "ふが"))
    }
  }

  post("/") {
    val hiya = parsedBody match {
      case obj:JObject => Try(obj.extract[Hiya]).getOrElse(halt(400))
      case _ => halt(400)
    }
    Ok(hiya)
  }

  error {
    case e => {
      logger.error("Error", e)
    }
  }

}
