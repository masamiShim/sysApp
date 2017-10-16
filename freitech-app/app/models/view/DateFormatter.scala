package models.view

import org.joda.time.DateTime
import java.text.SimpleDateFormat

object DateFormatter {
  val defaultFormat = "yyyy年MM月dd日 HH時mm分ss秒"

  /**
   * TODO:設定ファイルより読み込みができれば良い
   */
   
  def formatDefaultDate(date :DateTime) : String = {
    new SimpleDateFormat(defaultFormat).format(date)
  }
}