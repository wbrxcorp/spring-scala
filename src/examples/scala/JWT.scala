import scala.collection.JavaConversions._
import com.nimbusds.jose.{JWEHeader,EncryptionMethod,JWEAlgorithm,JOSEException}
import com.nimbusds.jose.crypto.{DirectEncrypter,DirectDecrypter}
import com.nimbusds.jwt.{JWTClaimsSet,EncryptedJWT}
import java.text.ParseException

object JWT {

	val sharedKey = "GLU9nOFeRfBUy89d".getBytes	/* 適当な暗号キー(共通鍵なので要秘匿) */

	/**
	 * 好きなデータ(custom claims)を与えて JWTを生成する
	 */
	def encode(customClaims:Map[String,Any]):String = {
		val builder = new JWTClaimsSet.Builder
		customClaims.foreach { case (key, value) => builder.claim(key, value) }

		val jwt = new EncryptedJWT(new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM), builder.build)
		jwt.encrypt(new DirectEncrypter(sharedKey))
		jwt.serialize
	}

	/**
	 * JWTから元のデータ(claims)を復元する
	 */
	def decode(s:String):Either[JWTClaimsSet,Exception] = {
		try {
			val jwt = EncryptedJWT.parse(s)
			jwt.decrypt(new DirectDecrypter(sharedKey))
			Left(jwt.getJWTClaimsSet)
		}
		catch {
			case e: ParseException => Right(e)
			case e: JOSEException  => Right(e)
		}
	}
	
	def main(args: Array[String]): Unit = {
		val jwt = encode(Map("name"->"俺様"))
		println("Encoded token: " + jwt)

		Seq(decode(jwt)/*success case*/, decode(jwt.replace('0', '1'))/*failure case*/).foreach { x=> x match {
			case Left(claimsSet) => println("Decoded custom claims: " + claimsSet.getClaims)
			case Right(e) => println("Decode error")
		}}
		
	}
}