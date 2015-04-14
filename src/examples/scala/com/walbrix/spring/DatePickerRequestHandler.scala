package com.walbrix.spring

import com.typesafe.scalalogging.slf4j.LazyLogging
import com.walbrix.spring.mvc.{Success, Result}
import org.joda.time.{LocalDate, DateTimeZone, DateTime}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestBody, ResponseBody, RequestMethod, RequestMapping}

/**
 * Created by shimarin on 15/04/14.
 */
case class Request(
  date:DateTime,  // JavaScriptの Dateオブジェクトを通信に乗せた時のタイムゾーン付き日時フォーマットを受け付けるフィールド
  formattedDate:LocalDate,  // YYYY-MM-DD形式の文字列を最も単純な日付データとして受け付けるフィールド
  timezone:Option[String] // "Asia/Tokyo" など
)

@Controller
@RequestMapping(Array("datepicker"))
class DatePickerRequestHandler extends LazyLogging {

  /**
   * 日付を受け取り、１ヶ月後の日付を返す
   */
  @RequestMapping(value=Array(""), method=Array(RequestMethod.POST))
  @ResponseBody
  def hello(@RequestBody request:Request):Result[DateTime] = {
    logger.info(request.date.toString)    // UTCに正規化されている日時

    val dateUserIntended = request.timezone.map { timezone =>
      request.date.toDateTime(DateTimeZone.forID(timezone))
    }.getOrElse(request.date)
    logger.info(dateUserIntended.toString)  // クライアント側のタイムゾーンで意図された日時

    logger.info(request.formattedDate.toString) // クライアント側でフォーマットされた文字列形式の日付をJodaでパースしたもの

    // それらはともかくとして、ここでは単純にdateに1ヶ月足して返す(クライアントがローカル時刻に勝手に戻すのでUTCのままで良い)
    val oneMonthAfter = request.date.plusMonths(1)
    logger.info(oneMonthAfter.toString)
    Success(oneMonthAfter)
  }
}
