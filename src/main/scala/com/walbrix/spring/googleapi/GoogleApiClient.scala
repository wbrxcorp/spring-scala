package com.walbrix.spring.googleapi

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by shimarin on 15/04/02.
 */

case class UserInfo(id:String, name:Option[String],
                    @JsonProperty("given_name") givenName:Option[String],
                    @JsonProperty("family_name") familyName:Option[String],
                    link:Option[String],picture:Option[String],gender:Option[String],locale:Option[String])

class GoogleApiClient {
  @Autowired private var objectMapper:ObjectMapper = _

  def getUserInfo(accessToken:String):UserInfo = {
    val conn = new java.net.URL("https://www.googleapis.com/oauth2/v2/userinfo").openConnection()
    conn.addRequestProperty("Authorization", "Bearer %s".format(accessToken))
    val is = conn.getInputStream
    try {
      objectMapper.readValue(conn.getInputStream, classOf[UserInfo])
    }
    finally {
      is.close
    }
  }
}
