package com.walbrix.spring

import collection.JavaConversions._

import org.springframework.web.bind.annotation.{RequestMethod, RequestMapping, RestController}
import twitter4j.{Status, TwitterFactory}
import twitter4j.conf.ConfigurationBuilder

/**
 * Created by shimarin on 15/05/02.
 */

case class TwitterConfig(consumerKey:String,consumerSecret:String,accessToken:String,accessTokenSecret:String)

@RestController
@RequestMapping(value=Array("twitter"))
class TwitterRequestHandler extends SystemConfigSupport {

  def twitter():twitter4j.Twitter = {
    val config = getSystemConfig[TwitterConfig]("twitter").getOrElse(throw new RuntimeException)
    val configurationBuilder = new ConfigurationBuilder
    configurationBuilder.setOAuthConsumerKey(config.consumerKey)
    configurationBuilder.setOAuthConsumerSecret(config.consumerSecret)
    configurationBuilder.setOAuthAccessToken(config.accessToken)
    configurationBuilder.setOAuthAccessTokenSecret(config.accessTokenSecret)
    new TwitterFactory(configurationBuilder.build).getInstance
  }

  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  def foo():Seq[Status] = {
    twitter.timelines.getUserTimeline("wbrxcorp").map { status =>
      Option(status.getRetweetedStatus).getOrElse(status)
    }
  }
}
