package com.walbrix.spring

import java.io.StringWriter

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.apache.velocity.VelocityContext

/**
 * Created by shimarin on 15/04/06.
 */
object ApplyVariables extends LazyLogging {
  def apply(template:String,variables:Map[String,String]):String = {
    "\\$\\{[a-zA-Z0-9_]+\\}".r.replaceAllIn(template, { matched =>
      val original = matched.group(0)
      variables.get(original.replaceAll("^\\$\\{(.+)\\}$", "$1")).getOrElse(original)
    })
  }
}
