package com.walbrix.spring

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Created by shimarin on 15/05/03.
 */
trait MustBeLocalhost extends com.typesafe.scalalogging.slf4j.LazyLogging {
  @Autowired private var request:HttpServletRequest = _

  class MustBeLocalhostException(message:String) extends Exception(message)

  @ExceptionHandler(Array(classOf[MustBeLocalhostException]))
  def forbiddenBecauseNotLocalhost(ex:Exception, response:HttpServletResponse):Unit = {
    logger.debug(ex.getMessage)
    response.sendError(HttpServletResponse.SC_FORBIDDEN)
  }

  protected def mustBeLocalhost:Unit = {
    //http://stackoverflow.com/questions/8426171/what-regex-will-match-all-loopback-addresses
    val remoteAddr = request.getRemoteAddr
    """^127(?:\.[0-9]+){0,2}\.[0-9]+$|^(?:0*\:)*?:?0*1$""".r.findFirstMatchIn(remoteAddr).getOrElse(throw new MustBeLocalhostException(remoteAddr))
  }

}
