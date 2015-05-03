package com.walbrix.spring

import java.net.URL

/**
 * Created by shimarin on 15/05/01.
 */
object Amazon {
  // jodaのJavaDocによれば、DateTimeFormatやそれの返すフォーマッタははMT Safe
  val dateFormat = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZoneUTC

  private def urlencode(value:String):String =
    java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~")

  def createQueryString(accessKeyId:String, associateTag:Option[String],
                        operation:String, _args:Map[String,String]):String = {
    val args = scala.collection.SortedMap.newBuilder[String,String]
    args ++= _args
    args += "AWSAccessKeyId" -> accessKeyId
    associateTag.foreach(args += "AssociateTag" -> _)
    args += "Service" -> "AWSECommerceService"
    args += "Timestamp" -> dateFormat.print(System.currentTimeMillis())
    args += "Operation" -> operation
    args.result.map { kv => "%s=%s".format(kv._1, urlencode(kv._2)) }.mkString("&")
  }

  def createRequestURL(endpoint:String, queryString:String, secretAccessKey:String):String = {
    // Calculate signature
    val endpointURL = new URL(endpoint)
    val stringToSign =
      "%s\n%s\n%s\n%s".format("GET", endpointURL.getHost(), endpointURL.getPath(), queryString)
    val mac = javax.crypto.Mac.getInstance("HmacSHA256")
    mac.init(new javax.crypto.spec.SecretKeySpec(secretAccessKey.getBytes("UTF-8"), "HmacSHA256"))
    val signature = new String(org.apache.commons.codec.binary.Base64.encodeBase64(mac.doFinal(stringToSign.getBytes("UTF-8"))))

    // Append signature to the URL
    "%s?%s&Signature=%s".format(endpoint, queryString, urlencode(signature))
  }

  def createRequestURL(endpoint:String,
                       accessKeyId:String, associateTag:Option[String], secretAccessKey:String,
                       operation:String, args:Map[String,String]):String = {
    val queryString = createQueryString(accessKeyId, associateTag, operation, args)
    return createRequestURL(endpoint, queryString, secretAccessKey)
  }

  def apply(endpoint:String, accessKeyId:String, associateTag:Option[String], secretAccessKey:String, operation:String, args:Map[String,String]):scala.xml.Elem = {
    scala.xml.XML.load(new URL(createRequestURL(endpoint, accessKeyId,
      associateTag,
      secretAccessKey, operation, args)))
  }
}
