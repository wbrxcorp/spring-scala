package com.walbrix.spring

import org.springframework.web.bind.annotation._

/**
 * Created by shimarin on 15/04/01.
 */

/**
 * Googleの UserInfo APIの結果を受け取る case class
 */
case class UserInfo(
                     id:String,
                     email:Option[String],
                     verifiedEmail:Option[Boolean],
                     name:Option[String],
                     givenName:Option[String],
                     familyName:Option[String],
                     link:Option[String],
                     picture:Option[String],
                     gender:Option[String],
                     locale:Option[String])

@RestController
@RequestMapping(Array("google"))
class GoogleApiRequestHandler extends com.typesafe.scalalogging.slf4j.LazyLogging {
  private val userinfoURL = new java.net.URL("https://www.googleapis.com/oauth2/v2/userinfo")

  @RequestMapping(value=Array("userinfo"), method=Array(RequestMethod.GET))
  def userinfo(@RequestParam accessToken:String):UserInfo = {
    // URL接続をオープン
    val conn = userinfoURL.openConnection()
    // APIにアクセスするためのアクセストークンを Authorizationヘッダにセット
    conn.addRequestProperty("Authorization", "Bearer " + accessToken)

    // HTTPリクエストを実行してレスポンスの JSONをパースする
    val is = conn.getInputStream
    val userinfo = try(ObjectMapper.readValue[UserInfo](is)) finally(is.close)

    // 必要ならここでuserinfoの内容をセッションやデータベースに保存する

    userinfo
  }
}
