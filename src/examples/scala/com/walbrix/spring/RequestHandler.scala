package com.walbrix.spring

import com.fasterxml.jackson.annotation.JsonInclude
import com.walbrix.spring.mvc.HttpErrorStatus
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation._

/**
 * Created by shimarin on 15/02/01.
 */

trait Base extends HttpErrorStatus {
  case class Result(success:Boolean,info:Option[Any] = None)

  def test1(body:Map[String,Option[Any]]):Result

  @RequestMapping(value=Array("test1"), method = Array(RequestMethod.POST))
  @ResponseBody
  def _test1(@RequestBody body:Map[String,Option[Any]]):Result = test1(body)
}

@Controller
@Transactional
@RequestMapping(Array(""))
class RequestHandler extends Base with HttpContextSupport {
  override def test1(body:Map[String,Option[Any]]):Result = {
    println(body)
    Result(true)
  }

  @RequestMapping(value=Array("test405"), method=Array(RequestMethod.GET))
  def notallowed():Unit = {
    raiseMethodNotAllowed
  }

  @RequestMapping(value=Array("context"), method=Array(RequestMethod.GET))
  @ResponseBody
  def context():Either[Option[String], Nothing] = {
    Left(getResourceAsStream("/WEB-INF/web.xml").map(IOUtils.toString(_)))
  }

  def getVersion():Option[String] = {
    getResourceAsStream("/META-INF/MANIFEST.MF").map { is =>
      try {
        val manifest = new java.util.jar.Manifest(is)
        manifest.getMainAttributes.getValue("Implementation-Version")
      }
      finally {
        is.close
      }
    }
  }

  @RequestMapping(value=Array("info"), method=Array(RequestMethod.GET))
  @ResponseBody
  def info():Map[String,Any] = {
    Map("version"->getVersion().getOrElse("UNKNOWN"))
  }
}

case class Tasty[T](
                     header:Seq[{val key:String; val name:String}],
                     rows:Seq[T],
                     pagination:{val count:Int;val page:Int;val pages:Int;val size:Int},
                     @JsonInclude(JsonInclude.Include.NON_NULL) sortBy:Option[String],
                     @JsonInclude(JsonInclude.Include.NON_NULL) sortOrder:Option[String])


object Tasty {
  case class HeaderColumn(key:String,name:String)
  case class Pagination(count:Int,page:Int,pages:Int,size:Int)
  def apply[T](header:Seq[(String,String)],rows:Seq[T],
           count:Int/*pageSize*/, page:Int,size:Int/*total count*/,
           sortBy:Option[String]=None,sortOrder:Option[String]=None):Tasty[T] = {
    Tasty(
      header.map { case (key,name) => HeaderColumn(key, name)} , rows,
      Pagination(rows.size, page, ((size - 1) / count + 1), size),
      sortBy, sortOrder
    )
  }
  def offset(page:Int,count:Int):Int = (page - 1) * count
}

case class Page[T](count:Int, rows:Seq[T])

@Controller
@RequestMapping(Array("zip"))
class ZipCodeRequestHandler extends ScalikeJdbcSupport {
  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  @ResponseBody
  def get(@RequestParam(value="pageNumber", defaultValue="1") pageNumber:Int = 1,
          @RequestParam(value="pageSize", defaultValue="20") pageSize:Int = 20):Page[Map[String,Any]] = {
    Page(int(sql"select count(*) from zip_code").get, apply(
      sql"select * from zip_code limit ${pageSize} offset ${(pageNumber - 1) * pageSize}".toMap().list()
    ))
  }

  @RequestMapping(value=Array("tasty"), method=Array(RequestMethod.GET))
  @ResponseBody
  def get(@RequestParam(value="sort-by",required=false) _sortBy:String,
          @RequestParam(value="sort-order",required=false) _sortOrder:String,
          @RequestParam(value="page",defaultValue="1") page:Int,
          @RequestParam(value="count",defaultValue="20") count:Int):Tasty[Map[String,Any]] = {

    val (sortBy, sortOrder) = (Option(_sortBy), Option(_sortOrder))

    val header = Seq(
      "JIS_CODE"->"JISコード",
      "ZIP_CODE"->"郵便番号",
      "CITY_KANA"->"市区町村カナ",
      "STREET_KANA"-> "それ以降カナ",
      "PREF"->"都道府県",
      "CITY"->"市区町村",
      "STREET"->"それ以降"
    )
    val size = int(sql"select count(*) from zip_code").get
    val rows = apply(sql"select * from zip_code limit ${count} offset ${Tasty.offset(page, count)}".toMap().list())
    Tasty(
      header,// カラムヘッダ
      rows,  // 行
      count, // 1ページあたりの行数
      page,  // ページ(1-)
      size,  // 全件の行数
      sortBy, sortOrder)
  }
}
