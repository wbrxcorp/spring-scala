import com.nimbusds.jose.Payload
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWTClaimsSet
import java.util.Date
import java.util.UUID
import scala.collection.JavaConversions._
import com.nimbusds.jose.JWEHeader
import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jwt.EncryptedJWT
import com.nimbusds.jose.crypto.DirectEncrypter
import com.nimbusds.jose.crypto.DirectDecrypter
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet
import com.nimbusds.jose.JOSEException

object JWS {
	val sharedKey = "a0a2abd8-6162-41";
  
	def encodeJws() = {
	  // Create JWS payload
	  val payload = new Payload("Hello world!")
	  
	  // Create JWS header with HS256 algorithm
	  val header = new JWSHeader(JWSAlgorithm.HS256)
	  header.setContentType("text/plain")
	  
	  // Create JWS object
	  val jwsObject = new JWSObject(header, payload)
	  
	  // Create HMAC signer
	  val signer = new MACSigner(sharedKey.getBytes)
	  jwsObject.sign(signer)
	  
	  // Serialise JWS object to compact format
	  jwsObject.serialize
	}
	
	def decodeJws(s:String) = {
	  
	  // Parse back and check signature
	  val jwsObject = JWSObject.parse(s)
	  val verifier = new MACVerifier(sharedKey.getBytes())
	  val verifiedSignature = jwsObject.verify(verifier)
	  
	  println (verifiedSignature match {
	    case true => "Verified JWS signature!"
	    case false => "Bad JWS signature!"
	  })
	  
	  jwsObject
	}


	def main(args: Array[String]): Unit = {
	  val jws = encodeJws
	  println("Serialized JWS Object: " + jws)
	  println("Recovered payload message: " + decodeJws(jws).getPayload)
	}
}
