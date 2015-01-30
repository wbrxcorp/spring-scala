package com.walbrix.spring

import java.io.StringWriter

import org.apache.velocity.VelocityContext

import collection.JavaConversions._

import org.apache.velocity.app.{Velocity, VelocityEngine}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.velocity.VelocityEngineUtils

/**
 * Created by shimarin on 14/11/15.
 */
trait VelocitySupport {
  @Autowired private var velocityEngine:VelocityEngine = _

  def mergeTemplateIntoString(templateName:String,variables:Map[String,AnyRef]):String = {
    VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "confirm-email.vm","UTF-8", variables)
  }

  def evaluate(template:String,variables:Map[String,AnyRef],logTag:String="evaluate"):String = {
    val result = new StringWriter();
    Velocity.evaluate(new VelocityContext(variables), result, logTag, template)
    result.toString
  }
}
