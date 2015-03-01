package com.walbrix.spring

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.ApplicationContext
import org.springframework.context.support.{ClassPathXmlApplicationContext, FileSystemXmlApplicationContext}

/**
 * Created by shimarin on 15/02/26.
 */
class _DB extends ScalikeJdbcSupport {

}

object REPL {
  private var applicationContext:ApplicationContext = _

  def apply(name:String):AnyRef = applicationContext.getBean(name)

  def DB:_DB = apply("DB").asInstanceOf[_DB]
  def objectMapper:ObjectMapper = apply("objectMapper").asInstanceOf[ObjectMapper]

  def main(args:Array[String]):Unit = {
    applicationContext = new ClassPathXmlApplicationContext("/com/walbrix/spring/REPL.xml")
    System.setProperty("scala.usejavacp","true")
    val runner = Class.forName("scala.tools.nsc.MainGenericRunner")
    runner.getMethod("main", classOf[Array[String]]).invoke(null, args)
  }
}
