package com.walbrix.spring.mvc

/**
 * Created by shimarin on 14/11/17.
 */
sealed abstract class Result[T](success:Boolean, info:Option[T] = None) {
  def getSuccess():Boolean = success
  def getInfo():Option[T] = info
}
final case class Success[T](info:Option[T] = None) extends Result[T](true, info)
final case class Fail[T](info:Option[T] = None) extends Result[T](false, info)
case object Result {
  def apply[T](success:Boolean, info:Option[T] = None) = {
    if (success) new Success(info) else new Fail(info)
  }
}
case object Success {
  def apply[T](info:T) = new Success(Some(info))
}
case object Fail {
  def apply[T](info:T) = new Fail(Some(info))
}
