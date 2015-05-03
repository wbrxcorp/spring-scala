package com.walbrix.spring

import javax.naming.{NameNotFoundException, NamingException, InitialContext}

/**
 * Created by shimarin on 15/05/02.
 */
object JNDI {
  val ctx = try(Option(new InitialContext)) catch{ case ex:NamingException => None }

  def apply[T](name:String):Option[T] = synchronized {
    try (ctx.map(_.lookup(name).asInstanceOf[T])) catch {
      case ex:NameNotFoundException => None
    }
  }
}
