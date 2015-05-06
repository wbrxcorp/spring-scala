package com.walbrix.spring

import java.io.StringReader
import java.security.interfaces.RSAKey
import java.security.{KeyFactory, Security}
import java.security.spec.PKCS8EncodedKeySpec

import org.bouncycastle.asn1.pkcs.CertificationRequest
import org.bouncycastle.openssl.PEMReader
import org.springframework.web.bind.annotation.{RequestParam, RestController, RequestMapping}

case class PemInfo(`type`:String, subject:Option[String] = None)

/**
 * Created by shimarin on 15/05/06.
 */
@RestController
@RequestMapping(Array("pem"))
class PEMRequestHandler {
  @RequestMapping(Array(""))
  def dosomething(@RequestParam pem:String):PemInfo = {
    val sr = new StringReader(pem)
    new PEMReader(sr).readObject() match {
      case x:CertificationRequest =>
        PemInfo("csr", Some(x.getCertificationRequestInfo.getSubject.toString))
      case x:java.security.KeyPair =>
        PemInfo("key")
      case x:java.security.cert.X509Certificate =>
        PemInfo("cert", Some(x.getSubjectDN.toString))
      case _ =>
        throw new IllegalArgumentException("Unknown")
    }
  }
}

object PEMRequestHandler {
  Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider)
}