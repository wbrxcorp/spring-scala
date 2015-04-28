package com.walbrix.spring

import java.io.File

import _root_.ch.qos.logback.classic.{Level, Logger}
import org.slf4j.LoggerFactory
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.support.{ClassPathXmlApplicationContext, FileSystemXmlApplicationContext}

/**
 * Created by shimarin on 15/02/26.
 */
object REPLEnvironment {
  private var applicationContext:ApplicationContext = _

  def setApplicationContext(applicationContext: ApplicationContext):Unit = {
    this.applicationContext = applicationContext
  }

  def db:ScalikeJdbcSupport = {
    applicationContext.getBean(classOf[javax.sql.DataSource])
    val scalikeJdbcSupport = new AnyRef with ScalikeJdbcSupport
    applicationContext.getAutowireCapableBeanFactory.autowireBean(scalikeJdbcSupport)
    scalikeJdbcSupport
  }

  def objectMapper = applicationContext.getBean(classOf[com.fasterxml.jackson.databind.ObjectMapper])
}

object REPL {
  case class Config(verbose:Boolean=false,subcmd:(Config)=>Unit, file:Option[File]=None)

  def run(config:Config):Unit = {
    val applicationContext = config.file match {
      case Some(x) => new FileSystemXmlApplicationContext(x.getPath)
      case None =>
        try { new ClassPathXmlApplicationContext("applicationContext.xml") }
        catch { case ex:BeansException => new ClassPathXmlApplicationContext("/com/walbrix/spring/REPL.xml") }
    }
    REPLEnvironment.setApplicationContext(applicationContext)
    System.setProperty("scala.usejavacp","true")
    val runner = Class.forName("scala.tools.nsc.MainGenericRunner")
    println("Type\nimport com.walbrix.spring.REPLEnvironment._\nto import helper")
    runner.getMethod("main", classOf[Array[String]]).invoke(null, Array("-nc"))
  }

  def hello(config:Config):Unit = {
    println("Hello")
  }

  def main(args:Array[String]):Unit = {
    val config = new scopt.OptionParser[Config]("repl") {
      head("repl", "1.0")
      opt[Unit]('v', "verbose") action { (_, c) =>
        c.copy(verbose = true)
      }
      cmd("hello") action { (_, c) =>
        c.copy(subcmd = hello)
      }
      arg[File]("<file>") optional() action { (x, c) =>
        c.copy(file = Some(x))
      } text("Spring bean file")
      help("help") text("prints this usage text")
    }.parse(args, Config(subcmd=run)).getOrElse {
      // arguments are bad, usage message will have been displayed
      return
    }
    if (config.verbose) {
      LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger].setLevel(Level.DEBUG)
    }
    config.subcmd(config)
  }
}
