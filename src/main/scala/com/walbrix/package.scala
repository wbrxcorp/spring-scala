package com

import java.security.MessageDigest

/**
 * Created by shimarin on 14/11/17.
 */
package object walbrix {
  private def getSha256(str:String):String = {
    val buf = new StringBuffer()
    val md = MessageDigest.getInstance("SHA-256")
    md.update(str.getBytes())
    md.digest().foreach { b =>
      buf.append("%02x".format(b))
    }
    buf.toString
  }

  def comparePassword(encrypted:String, password:String):Boolean = {
    encrypted.split('$') match {
      case x if x.length == 2 => x(1) == getSha256("%s%s".format(x(0), password))
      case _ => false
    }
  }

  def encryptPassword(password:String):String = {
    val salt = new scala.util.Random(new java.security.SecureRandom()).alphanumeric.take(5).mkString
    "%s$%s".format(salt, getSha256("%s%s".format(salt, password)))
  }

  def generateAuthToken(id:String):String =
    "%s%s".format(getSha256(id), new scala.util.Random(new java.security.SecureRandom()).alphanumeric.take(16).mkString)
}
