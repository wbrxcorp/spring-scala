package com.walbrix.spring

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation._

/**
 * Created by shimarin on 15/04/07.
 */
@RestController
@Transactional
@RequestMapping(Array("smart-table"))
class SmartTableRequestHandler extends ScalikeJdbcSupport with LazyLogging {

  // このAPIがクライアントに返す結果の型
  case class Result[T](numberOfPages:Int, rows:Seq[T])

  // 検索条件の種別を表す Enum的なオブジェクト
  sealed abstract class COND {}

  object COND {
    case object PREFIX extends COND   // 前方一致検索
    case object PARTIAL extends COND  // 部分一致検索
    case object ENTIRE extends COND   // 完全一致検索
  }

  // このAPIで使用されるデータベースカラムの定義。ScalikeJDBCでは文字列をSQLと連結することができない（正しい！）ため、
  // 文字列のカラム名と SQL断片としてのカラム名を対応付けるデータをここに作成しておく。ついでに検索のタイプもここで決める。
  // コード類は前方一致検索、名称類は部分一致検索とする。ただし都道府県については部分一致が必要と考えにくいため完全一致。
  val column:Map[String,(SQLSyntax, COND)] = Map(
    "JIS_CODE"->(sqls"JIS_CODE", COND.PREFIX),
    "ZIP_CODE"->(sqls"ZIP_CODE", COND.PREFIX),
    "CITY_KANA"->(sqls"CITY_KANA", COND.PARTIAL),
    "STREET_KANA"->(sqls"STREET_KANA", COND.PARTIAL),
    "PREF"->(sqls"PREF", COND.ENTIRE),
    "CITY"->(sqls"CITY", COND.PARTIAL),
    "STREET"->(sqls"STREET", COND.PARTIAL)
  )

  /**
   * 郵便番号データベースに問い合わせを行い結果を返す
   */
  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  def get(@RequestParam(value="start", defaultValue="0") start:Int = 0, // オフセット
          @RequestParam(value="number", defaultValue="10") number:Int = 10, // ページあたりの行数
          @RequestParam(value="sort", required=false) sort:String,  // ソートに使用するカラム名
          @RequestParam(value="reverse", required=false) reverse:Boolean, // ソートの方向
          @RequestParam search:java.util.Map[String,String] /*各カラムに与えられた検索文字列*/):Result[Map[String,Any]] = {

    // where句の構築
    val whereClause = column.map { case (name, (col, cond)) =>
      Option(search.get(name)).map { x => // JavaのMapなのでgetの返り値を一旦Optionで包んでやらないといけない
        cond match {  // カラムの検索タイプに対応したSQL式を構築
          case COND.PREFIX => sqls"${col} like ${x + "%"}"
          case COND.PARTIAL => sqls"${col} like ${"%" + x + "%"}"
          case COND.ENTIRE => sqls"${col}=${x}"
        }
      }
    }.flatten.foldLeft(None:Option[SQLSyntax]) { (stx, cond) => // 式をandで連結して where句を構築
      stx.map(_.append(sqls" and ").append(cond)).orElse(Some(sqls"where ".append(cond)))
    }.getOrElse(sqls"")

    // order by句の構築 sort:ソートに使用されるカラム名, reverse:向き
    val orderByClause = Seq(
      Option(sort).map { x=> (column(x)._1, if (reverse) sqls"desc" else sqls"asc") },
      Some((sqls"ZIP_CODE",sqls"asc")) // order by句の最後に付く最も弱いソート条件
    ).flatten.foldLeft(None:Option[SQLSyntax]) { (stx, cond) => // order by式をカンマで連結
      stx.map(_.append(sqls",").append(cond._1).append(cond._2)).
        orElse(Some(sqls"order by  ".append(cond._1).append(cond._2)))
    }.getOrElse(sqls"")

    Result(
      // 該当件数から最大ページ数を算出
      int(sql"select (count(*) - 1) / ${number} + 1 from zip_code ${whereClause}").get,
      // 与えられたページネーション用パラメータ（オフセットとページあたりの行数）でクエリを実行
      list(
        sql"select * from zip_code ${whereClause} ${orderByClause} limit ${number} offset ${start}".toMap
      )
    )
  }

}
