package com.walbrix.spring.mvc

import org.joda.time.DateTime
import org.springframework.web.bind.annotation._

/**
 * Created by shimarin on 14/11/17.
 */
case class Page[T](count:Int,offset:Int,limit:Int,items:Seq[T])

abstract trait CRUD[T,TID] extends HttpErrorStatus {
  def defaultLimit() = 20
  def toIdType(id:String):TID

  class Entity(map:Map[String,AnyRef]) {
    private def missing(name:String):String = "Missing request property '%s'".format(name)

    def apply(name:String):AnyRef = map.get(name).getOrElse(raiseBadRequest(missing(name)))
    def get(name:String):Option[AnyRef] = map.get(name)

    def int(name:String):Int = intOpt(name).getOrElse(raiseBadRequest(missing(name)))
    def intOpt(name:String):Option[Int] = map.get(name).map(_.asInstanceOf[Int])
    def string(name:String):String = stringOpt(name).getOrElse(raiseBadRequest(missing(name)))
    def stringOpt(name:String):Option[String] = map.get(name).map(_.asInstanceOf[String])
    def boolean(name:String):Boolean = booleanOpt(name).getOrElse(raiseBadRequest(missing(name)))
    def booleanOpt(name:String):Option[Boolean] = map.get(name).map(_.asInstanceOf[Boolean])
    def jodaDateTime(name:String):DateTime = jodaDateTimeOpt(name).getOrElse(raiseBadRequest(missing(name)))
    def jodaDateTimeOpt(name:String):Option[DateTime] = map.get(name) match {
      case None | Some(null) => None
      case Some(null) => Some(null)
      case Some(x) => new Some(new DateTime(x))
    }
  }

  @RequestMapping(value=Array(""), method=Array(RequestMethod.POST), consumes=Array("application/json"))
  @ResponseBody
  def _create(@RequestBody entity:Map[String,AnyRef]):Result[TID] =
    create(new Entity(entity)).map(Success(_)).getOrElse(Fail())

  def create(entity:Entity):Option[TID]

  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  @ResponseBody
  def _get(@RequestParam(value="offset",defaultValue="0") offset:Int,
           @RequestParam(value="limit",required=false) _limit:java.lang.Integer,
           @RequestParam(value="ordering",required=false) ordering:String):Page[T] = {
    val limit = if (_limit == null) defaultLimit() else _limit.toInt
    val rst = get(offset, limit, Option(ordering))
    Page(rst._1, offset, limit, rst._2)
  }

  def get(offset:Int, limit:Int, ordering:Option[String] = None):(Int, Seq[T])

  @RequestMapping(value=Array("{id:.+}"), method=Array(RequestMethod.GET))
  @ResponseBody
  def _get(@PathVariable(value="id") id:String):T = {
    get(toIdType(id)).getOrElse(raiseNotFound)
  }

  def get(id:TID):Option[T]

  @RequestMapping(value=Array("{id:.+}"), method=Array(RequestMethod.POST), consumes=Array("application/json"))
  @ResponseBody
  def _update(@PathVariable("id") id:String, @RequestBody entity:Map[String,AnyRef]):T = {
    val properId = toIdType(id)
    (try {
      update(properId,new Entity(entity))
    }
    catch  {
      case e:Exception => raiseBadRequest
    }) match {
      case true => get(properId).getOrElse(raiseNotFound)
      case false => raiseNotFound
    }
  }

  def update(id:TID, entity:Entity):Boolean

  @RequestMapping(value=Array("{id:.+}"), method=Array(RequestMethod.DELETE))
  @ResponseBody
  def _delete(@PathVariable("id") id:String):Result[String] = {
    (try {
      delete(toIdType(id))
    }
    catch  { case e:Exception => return Fail(e.getMessage) })
    match {
      case true => Success()
      case false => raiseNotFound
    }
  }

  def delete(id:TID):Boolean
}

trait CRUDWithAuthentication[T,TID,U,UID] extends CRUD[T,TID] with Authentication[U,UID] {
  override def create(entity: Entity): Option[TID] = create(entity, getUser())
  def create(entity:Entity, user:U):Option[TID]

  override def update(id: TID, entity: Entity): Boolean = update(id, entity, getUser())
  def update(id:TID, entity:Entity, user:U):Boolean

  override def get(offset: Int, limit: Int, ordering: Option[String]): (Int, Seq[T]) = get(offset, limit, ordering, getUser())
  def get(offset: Int, limit: Int, ordering: Option[String], user:U):(Int, Seq[T])

  override def get(id: TID): Option[T] = get(id, getUser())
  def get(id:TID, user:U):Option[T]

  override def delete(id: TID): Boolean = delete(id, getUser())
  def delete(id:TID, user:U):Boolean
}
