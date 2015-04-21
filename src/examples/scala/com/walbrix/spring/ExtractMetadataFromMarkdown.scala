package com.walbrix.spring

/**
 * Created by shimarin on 15/04/21.
 */
object ExtractMetadataFromMarkdown {
  val metadataRegex ="""^(.+:.+\n)+\n""".r

  def apply(md:String):(String,Map[String,String]) = {
    metadataRegex.findFirstIn(md) match {
      case Some(metadata) =>
        val metamap = """(?m)^(.+:.+)$""".r.findAllIn(metadata).map { single =>
          val splitted = single.split(":")
          (splitted(0).trim, splitted(1).trim)
        }.toMap
        //logger.debug(metadataRegex.split(md).toSeq.toString)
        ((metadataRegex.split(md) ++ Array("","")).apply(1), metamap)
      case None => (md, Map())
    }
  }


}
