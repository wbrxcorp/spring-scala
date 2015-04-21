package com.walbrix.spring

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/**
 * Created by shimarin on 15/04/01.
 */

case class UserInfo(id:String, name:Option[String],
                    @JsonProperty("given_name") givenName:Option[String],
                    @JsonProperty("family_name") familyName:Option[String],
                    link:Option[String],picture:Option[String],gender:Option[String],locale:Option[String])

@RestController
@RequestMapping(Array("google"))
class GoogleApiRequestHandler {
  @Autowired private var objectMapper:com.fasterxml.jackson.databind.ObjectMapper = _

  private val userinfoURL = new java.net.URL("https://www.googleapis.com/oauth2/v2/userinfo")

  @RequestMapping(value=Array("userinfo"), method=Array(RequestMethod.GET))
  def hello(@RequestParam accessToken:String):UserInfo = {
    val conn = userinfoURL.openConnection()
    conn.addRequestProperty("Authorization", "Bearer %s".format(accessToken))
    val is = conn.getInputStream
    val userinfo = try (objectMapper.readValue(is, classOf[UserInfo])) finally(is.close)
    // 必要ならここでuserinfoの内容をセッションやデータベースに保存する
    userinfo
  }

}
