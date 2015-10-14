package com.walbrix.scalatra

import scala.util.Try

import org.json4s.JObject
import org.scalatra.Ok

/**
 * Created by shimarin on 15/10/14.
 */
case class Hiya(abc:Option[Int], hoge:String)

class ExampleServlet
  extends org.scalatra.ScalatraServlet with org.scalatra.json.JacksonJsonSupport
  with com.walbrix.spring.ScalikeJdbcSupport with com.walbrix.spring.TransactionSupport
  with com.typesafe.scalalogging.slf4j.LazyLogging {
  override protected implicit def jsonFormats: org.json4s.Formats = org.json4s.DefaultFormats.withBigDecimal ++ org.json4s.ext.JodaTimeSerializers.all

  override def initialize(config:javax.servlet.ServletConfig):Unit = {
    org.springframework.web.context.support.SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext)
  }

  before() {
    println("Before")
  }

  after() {
    println("After")
  }

  get("/") {
    contentType = formats("json")
    tx {
      val two = single(sql"select 1+1".map(_.int(1)))
      val three = single(sql"select 1+2".map(_.int(1)))
      Ok(Hiya(two, "ふが"))
    }
  }

  post("/") {
    val hiya = parsedBody match {
      case obj:JObject => Try(obj.extract[Hiya]).recover { case e=>halt(400, e) }.get
      case _ => halt(400, "Request body must be a json object")
    }
    Ok(hiya)
  }

  error {
    case e => {
      logger.error("Error", e)
    }
  }

}
