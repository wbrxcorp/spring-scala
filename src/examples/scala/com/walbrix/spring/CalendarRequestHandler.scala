package com.walbrix.spring

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import org.joda.time.LocalDate
import org.springframework.web.bind.annotation._

import scala.collection.mutable

/**
 * Created by shimarin on 15/09/17.
 */
@RestController
@RequestMapping(Array("calendar"))
class CalendarRequestHandler {

  case class Day(
                  id:String, // e.g. "2015-09-15"
                  year:Int, // e.g. 2015
                  month:Int, // e.g. 9
                  day:Int, // e.g. 15
                  dow:Int // 0 = Sun, 6 = Sat
                  )

  @JsonInclude(Include.NON_NULL)
  case class Month(
                    id:String, // e.g. "2015-09"
                    year:Int, // e.g. 2015
                    month:Int, // e.g. 9
                    weeks:Option[Array[Array[Day]]]=None,  // [Sun,Mon,Tue,Wed,Thu,Fri,Sat]
                    prev:Option[Month]=None,  // previous month
                    next:Option[Month]=None // next month
                    )
  object Month {
    def fromLocalDate(localDate: LocalDate, weeks:Option[Array[Array[Day]]]=None,prev:Option[Month]=None,next:Option[Month]=None):Month = Month("%4d-%02d".format(localDate.getYear, localDate.getMonthOfYear), localDate.getYear, localDate.getMonthOfYear, weeks, prev, next)
  }

  @RequestMapping(value=Array("{year}-{month}"), method=Array(RequestMethod.GET))
  def get(@PathVariable year:Int, @PathVariable month:Int, @RequestParam(defaultValue="false") startsWithMonday:Boolean):Month = {
    val firstDate = new LocalDate(year, month, 1)
    val lastDate = firstDate.dayOfMonth.withMaximumValue // http://stackoverflow.com/questions/9711454/how-to-get-the-last-date-of-a-particular-month-with-jodatime

    val weeks = mutable.Queue[Array[Day]]()
    Range(1,lastDate.dayOfMonth.getMaximumValue + 1).foreach { day =>
      val week = weeks.lastOption.filter(_(6) == null) match {
        case None =>
          weeks += new Array[Day](7)
          weeks.last
        case Some(week) => week
      }
      val dow = new LocalDate(year, month, day).getDayOfWeek
      val index = if (startsWithMonday) dow - 1 else dow % 7
      week(index) = Day("%4d-%02d-%02d".format(year, month, day), year, month,day, dow % 7)
    }

    Month.fromLocalDate(firstDate, Some(weeks.toArray), Some(Month.fromLocalDate(firstDate.minusMonths(1))), Some(Month.fromLocalDate(firstDate.plusMonths(1))))
  }

  @RequestMapping(value=Array(""), method=Array(RequestMethod.GET))
  def get(@RequestParam(defaultValue="false") startsWithMonday:Boolean=false):Month = {
    val localDate = new LocalDate()
    get(localDate.getYear, localDate.getMonthOfYear, startsWithMonday)
  }
}
