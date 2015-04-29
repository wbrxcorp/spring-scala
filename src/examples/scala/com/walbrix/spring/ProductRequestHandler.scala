package com.walbrix.spring

import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, RequestMapping, RestController}

/**
 * Created by shimarin on 15/04/29.
 */

case class Product(id:String,title:String,price:Int,available:Boolean, tags:Seq[Tag])
case class Tag(text:String)

@RestController
@RequestMapping(value=Array("product"))
class ProductRequestHandler extends ScalikeJdbcSupport {
  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  def products():Seq[Product] = {
    list(sql"select * from products,product_tags where products.id=product_tags.product_id".one { row =>
      Product(row.string("id"), row.string("title"), row.int("price"), row.boolean("available"), Nil)
    }.toMany(_.stringOpt("tag").map(Tag(_))).map((one,many) => one.copy(tags=many)) )
  }

  @RequestMapping(value=Array("tags"), method=Array(RequestMethod.GET))
  def tags(@RequestParam(value="query",defaultValue="") query:String):Set[Tag] = {
    list(sql"select distinct tag from product_tags where tag like ${query + "%"}".map { row =>
      Tag(row.string(1))
    }).toSet
  }

}
