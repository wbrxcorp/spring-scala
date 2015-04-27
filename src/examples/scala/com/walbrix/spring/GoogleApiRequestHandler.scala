package com.walbrix.spring

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
 * Created by shimarin on 15/04/01.
 */

case class UserInfo(
                     id:String,
                     email:Option[String],
                     @JsonProperty("verified_email") verifiedEmail:Option[Boolean],
                     name:Option[String],
                     @JsonProperty("given_name") givenName:Option[String],
                     @JsonProperty("family_name") familyName:Option[String],
                     link:Option[String],
                     picture:Option[String],
                     gender:Option[String],
                     locale:Option[String])

@RestController
@RequestMapping(Array("google"))
class GoogleApiRequestHandler extends com.typesafe.scalalogging.slf4j.LazyLogging {
  @Autowired private var objectMapper:com.fasterxml.jackson.databind.ObjectMapper = _

  private val userinfoURL = new java.net.URL("https://www.googleapis.com/oauth2/v2/userinfo")

  type Closable = { def close():Unit }
  def using[A <: Closable,B]( resource:A )( f:A => B ) = try(f(resource)) finally(resource.close)

  @RequestMapping(value=Array("userinfo"), method=Array(RequestMethod.GET))
  def userinfo(@RequestParam accessToken:String):UserInfo = {
    val conn = userinfoURL.openConnection()
    conn.addRequestProperty("Authorization", "Bearer %s".format(accessToken))

    val is = logger.underlying.isDebugEnabled match {
      case true =>  // デバッグログが有効なら一度APIの実行結果をを全部読み出してログに出力する
        val content = using(conn.getInputStream)(org.apache.commons.io.IOUtils.toByteArray(_))
        logger.debug(new String(content, "UTF-8"))
        new java.io.ByteArrayInputStream(content)
      case false => conn.getInputStream
    }

    val userinfo = using(is)(objectMapper.readValue(_, classOf[UserInfo]))
    // 必要ならここでuserinfoの内容をセッションやデータベースに保存する
    userinfo
  }

}
