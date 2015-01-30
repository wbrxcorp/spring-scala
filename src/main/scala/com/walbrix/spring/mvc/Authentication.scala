package com.walbrix.spring.mvc

import javax.servlet.http.Cookie

import com.walbrix.spring.HttpContextSupport
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.{RequestBody, ResponseBody, RequestMethod, RequestMapping}

/**
 * Created by shimarin on 14/11/17.
 */
abstract trait Authentication[U,UID] extends HttpContextSupport with HttpErrorStatus {
  val authTokenCookieName = "auth_token"
  val userIdSessionAttrKey = "user_id"
  val userRequestAttrKey = "user"
  val authCookieAge = 60*60*24*180

  def checkPassword(username:String, password:String):Option[(UID,String)]
  def resetAuthToken(userId:UID):Option[String]
  def getIdFromUser(user:U):UID
  def getUser(userId:UID):Option[U]
  def getUserByAuthToken(authToken:String):Option[U]

  private def sendCredentials(userId:UID, authToken:String):Unit = {
    val session = getSession()
    session.invalidate()
    val cookie: Cookie = new Cookie(authTokenCookieName, authToken)
    cookie.setPath("/")
    cookie.setMaxAge(authCookieAge)
    addCookie(cookie)
    val newSession = getSession()
    newSession.setAttribute(userIdSessionAttrKey, userId)
  }

  def login(username:String, password:String):Boolean = {
    checkPassword(username, password).map { case (userId,authToken) =>
      sendCredentials(userId, authToken)
      true
    }.getOrElse(false)
  }

  def loginWithFreshAuthToken(userId:UID):Boolean = {
    resetAuthToken(userId).map { authToken =>
      sendCredentials(userId, authToken)
      true
    }.getOrElse(false)
  }

  def logout():Boolean = {
    resetAuthToken(getIdFromUser(getUser()))
    getSession().invalidate()
    true
  }

  def getAuthToken():Option[String] = {
    Option(getCookies).flatMap(_.find(_.getName.equals(authTokenCookieName)).map(_.getValue))
  }

  def getUserOpt():Option[U] = {
    Option(getAttribute(userRequestAttrKey).asInstanceOf[U]).foreach { user =>
      return Some(user)
    }
    val session = getSession()
    Option(session.getAttribute(userIdSessionAttrKey).asInstanceOf[UID]).foreach { userId =>
      getUser(userId).foreach { user =>
        setAttribute(userRequestAttrKey, user.asInstanceOf[AnyRef])
        return Some(user)
      }
    }
    getAuthToken.foreach { authToken =>
      getUserByAuthToken(authToken).foreach { user =>
        setAttribute(userRequestAttrKey, user.asInstanceOf[AnyRef])
        session.setAttribute(userIdSessionAttrKey, getIdFromUser(user)  )
        return Some(user)
      }
    }
    None
  }

  def getUser():U = getUserOpt().getOrElse(raiseForbidden)

}

abstract class AuthenticationBean[U,UID] extends Authentication[U,UID] {
  override def equals(obj:Any):Boolean = {
    obj match {
      case x:String if x.equals("auth") => getUserOpt().map(_.isInstanceOf[U]).getOrElse(false)
      case x => super.equals(x)
    }
  }
}

case class Auth(username:String, password:String)

trait LoginRequestHandler[U,UID] extends Authentication[U,UID] {
  @RequestMapping(value=Array("login"), method = Array(RequestMethod.GET))
  @ResponseBody
  @Transactional
  def login_():Map[String,AnyRef] = {
    Map("current_user"->getUserOpt())
  }

  @RequestMapping(value=Array("login"), method = Array(RequestMethod.POST),consumes=Array("application/json"))
  @ResponseBody
  def login(@RequestBody auth:Auth):Result[Nothing] = Result(login(auth.username, auth.password))

  @RequestMapping(value=Array("logout"), method = Array(RequestMethod.POST))
  @ResponseBody
  def logout_():Result[Nothing] = Result(logout())
}