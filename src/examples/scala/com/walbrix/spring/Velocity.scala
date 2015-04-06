package com.walbrix.spring

import java.io.StringWriter

import org.apache.velocity.VelocityContext

/**
 * Created by shimarin on 15/04/06.
 */
object Velocity {
  def evaluate(template:String,variables:Map[String,AnyRef],logTag:String="evaluate"):String = {
    val result = new StringWriter()
    val context = new VelocityContext()
    variables.foreach { case (key, value) =>
      context.put(key, value)
    }
    org.apache.velocity.app.Velocity.evaluate(context, result, logTag, template)
    result.toString
  }
}
