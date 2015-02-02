package com.walbrix.spring.mvc

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by shimarin on 14/11/03.
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="NOT FOUND")  // 404
class NotFoundException(message:String="NOT FOUND") extends RuntimeException(message) {
}

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="BAD REQUEST")  // 400
class BadRequestException(message:String="BAD REQUEST") extends RuntimeException(message) {
}

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="FORBIDDEN")  // 403
class ForbiddenException(message:String="FORBIDDEN") extends RuntimeException(message) {
}

@ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED, reason="METHOD NOT ALLOWED")  // 405
class MethodNotAllowedException(message:String="METHOD NOT ALLOWED") extends RuntimeException(message) {
}

trait HttpErrorStatus {
  def raiseNotFound = throw new NotFoundException
  def raiseNotFound(message:String) = throw new NotFoundException(message)
  def raiseBadRequest = throw new BadRequestException
  def raiseBadRequest(message:String) = throw new BadRequestException(message)
  def raiseForbidden = throw new ForbiddenException
  def raiseForbidden(message:String) = throw new ForbiddenException(message)
  def raiseMethodNotAllowed = throw new MethodNotAllowedException
  def raiseMethodNotAllowed(message:String) = throw new MethodNotAllowedException(message)
}
