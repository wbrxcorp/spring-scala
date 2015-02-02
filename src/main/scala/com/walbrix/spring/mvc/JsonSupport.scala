package com.walbrix.spring.mvc

import java.io.{InputStream, OutputStream}

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by shimarin on 15/02/02.
 */
trait JsonSupport extends HttpErrorStatus {
  @Autowired private var objectMapper:ObjectMapper = _

  class ObjectNodeHelper(node:ObjectNode) {
    def apply(name:String):Any = {
      get(name).getOrElse(raiseBadRequest("Property '" + name + "' is mandatory"))
    }
    def get(name:String):Option[Any] = {
      Option(node.get(name)).map { x=>
        x match {
          case x if (x.isInt) => x.intValue()
          case x if (x.isTextual) => x.textValue()
          case x if (x.isNull) => null
          case _ => raiseBadRequest("The type of field '" + name + "' is unsupported")
        }
      }
    }
    def stringOpt(name:String):Option[String] =  Option(node.get(name)).map(_.asText())
    def string(name:String):String = stringOpt(name).getOrElse(raiseBadRequest("Property '" + name + "' is mandatory"))
    def intOpt(name:String):Option[Int] =  Option(node.get(name)).map(_.asInt())
    def int(name:String):Int = intOpt(name).getOrElse(raiseBadRequest("Property '" + name + "' is mandatory"))
  }
  object ObjectNodeHelper {
    def apply(node:JsonNode):ObjectNodeHelper = {
      if (!node.isInstanceOf[ObjectNode]) {
        raiseBadRequest("Request body must be a JSON object")
      }
      new ObjectNodeHelper(node.asInstanceOf[ObjectNode])
    }
  }

  def toJson(value:AnyRef):String = objectMapper.writeValueAsString(value)
  def toJson(out:OutputStream, value:AnyRef) = objectMapper.writeValue(out, value)
  def fromJson[T](content:String):T = objectMapper.readValue(content, classOf[T])
  def fromJson[T](in:InputStream):T = objectMapper.readValue(in, classOf[T])
}
