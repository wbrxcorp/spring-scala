package com.walbrix.spring

import java.net.{URL, URLEncoder}
import java.text.SimpleDateFormat
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import org.apache.commons.codec.binary.Base64

import scala.collection.SortedMap
import scala.xml.{XML, Elem}

/**
 * Created by shimarin on 2015/03/28.
 */
class AmazonService {
  private val method = "GET"

  private val dateFormatter = new ThreadLocal[SimpleDateFormat]() {
    override def initialValue():SimpleDateFormat = {
      val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
      sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
      sdf
    }
  }

  private def urlencode(value:String):String = {
    URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~")
  }

  def createQueryString(accessKeyId:String, associateTag:Option[String],
                        operation:String, _args:Map[String,String]):String = {
    val args = SortedMap.newBuilder[String,String]
    args ++= _args
    args += "AWSAccessKeyId" -> accessKeyId
    associateTag.foreach(args += "AssociateTag" -> _)
    args += "Service" -> "AWSECommerceService"
    args += "Timestamp" -> dateFormatter.get.format(new java.util.Date())
    args += "Operation" -> operation
    args.result.map { kv => "%s=%s".format(kv._1, urlencode(kv._2)) }.mkString("&")
  }

  def createRequestURL(endpoint:String, queryString:String, secretAccessKey:String):String = {
    // Calculate signature
    val endpointURL = new URL(endpoint)
    val stringToSign =
      "%s\n%s\n%s\n%s".format(method, endpointURL.getHost(), endpointURL.getPath(), queryString)
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(new SecretKeySpec(secretAccessKey.getBytes("UTF-8"), "HmacSHA256"))
    val signature = new String(Base64.encodeBase64(mac.doFinal(stringToSign.getBytes("UTF-8"))))

    // Append signature to the URL
    "%s?%s&Signature=%s".format(endpoint, queryString, urlencode(signature))
  }

  def createRequestURL(endpoint:String,
                       accessKeyId:String, associateTag:Option[String], secretAccessKey:String,
                       operation:String, args:Map[String,String]):String = {
    val queryString = createQueryString(accessKeyId, associateTag, operation, args)
    return createRequestURL(endpoint, queryString, secretAccessKey)
  }

  private var endpoint:String = "http://ecs.amazonaws.jp/onca/xml"
  private var accessKeyId:String = _
  private var associateTag:Option[String] = None
  private var secretAccessKey:String = _

  def setEndpoint(endpoint:String):Unit = this.endpoint = endpoint
  def setAccessKeyId(accessKeyId:String):Unit = this.accessKeyId = accessKeyId
  def setAssociateTag(associateTag:String):Unit = this.associateTag = Option(associateTag)
  def setSecretAccessKey(secretAccessKey:String):Unit = this.secretAccessKey = secretAccessKey

  def query(operation:String, args:Map[String,String]):Elem = {
    XML.load(new URL(createRequestURL(endpoint, accessKeyId, associateTag, secretAccessKey, operation, args)))
  }
}
