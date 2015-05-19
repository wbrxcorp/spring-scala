package com.walbrix.spring

import java.util.Date
import javax.servlet.http.HttpServletResponse

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.web.bind.annotation._

@JsonInclude(JsonInclude.Include.NON_NULL)
case class PemInfo(`type`:String, subject:Option[String] = None, notAfter:Option[Date] = None)

/**
 * Created by shimarin on 15/05/06.
 */
@RestController
@RequestMapping(Array("pem"))
class PEMRequestHandler extends com.typesafe.scalalogging.slf4j.LazyLogging {
  @ExceptionHandler(Array(classOf[IllegalArgumentException]))
  def badrequest(ex:Exception, response:HttpServletResponse):Unit = {
    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage)
  }

  @RequestMapping(value=Array(""), method=Array(RequestMethod.POST))
  def dosomething(@RequestParam pem:String):PemInfo = {
    val sr = new java.io.StringReader(pem)
    new org.bouncycastle.openssl.PEMReader(sr).readObject() match {
      case x:org.bouncycastle.asn1.pkcs.CertificationRequest =>
        PemInfo("csr", Some(x.getCertificationRequestInfo.getSubject.toString))
      case x:java.security.KeyPair =>
        PemInfo("key")
      case x:java.security.cert.X509Certificate =>
        PemInfo("cert", Some(x.getSubjectDN.toString), Some(x.getNotAfter))
      case _ =>
        throw new IllegalArgumentException("Not a valid PEM or unknonwn type of content")
    }
  }
}
