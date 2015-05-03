package com.walbrix.spring

import java.net.{URL, URLEncoder}
import java.text.SimpleDateFormat
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.apache.commons.codec.binary.Base64

import scala.collection.SortedMap
import scala.xml.{XML, Elem}

/**
 * Created by shimarin on 2015/03/28.
 */
trait AmazonAccessKeyProvider {
  def getAccessKeyId():String
  def getAssociateTag():Option[String]
  def getSecretAccessKey():String
}

class SimpleAmazonAccessKeyProvider extends AmazonAccessKeyProvider {
  private var accessKeyId:String = _
  private var associateTag:Option[String] = None
  private var secretAccessKey:String = _

  def setAccessKeyId(accessKeyId:String):Unit = this.accessKeyId = accessKeyId
  def setAssociateTag(associateTag:String):Unit = this.associateTag = Option(associateTag match { case "" => null case x=>x})
  def setSecretAccessKey(secretAccessKey:String):Unit = this.secretAccessKey = secretAccessKey

  override def getAccessKeyId(): String = this.accessKeyId
  override def getSecretAccessKey(): String = this.secretAccessKey
  override def getAssociateTag(): Option[String] = this.associateTag
}

class AmazonService extends AnyRef with LazyLogging {
  private val method = "GET"
  private var endpoint:String = "http://ecs.amazonaws.jp/onca/xml"
  def setEndpoint(endpoint:String):Unit = this.endpoint = endpoint

  private var accessKeyProvider:AmazonAccessKeyProvider = _
  def setAccessKeyProvider(accessKeyProvider: AmazonAccessKeyProvider):Unit = this.accessKeyProvider = accessKeyProvider


  def query(operation:String, args:Map[String,String]):Elem = {
    Amazon(endpoint, this.accessKeyProvider.getAccessKeyId,
      this.accessKeyProvider.getAssociateTag,
      this.accessKeyProvider.getSecretAccessKey, operation, args)
  }
}
