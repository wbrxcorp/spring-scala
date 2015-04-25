package com.walbrix.spring

/**
 * Created by shimarin on 15/04/26.
 */
object Sha1Hash {
  def apply(str:String):String =
    org.apache.commons.codec.binary.Hex.encodeHexString(java.security.MessageDigest.getInstance("SHA-1").digest(str.getBytes("UTF-8")))
}
