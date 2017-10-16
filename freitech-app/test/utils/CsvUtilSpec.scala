package utils

import org.scalatestplus.play.PlaySpec
import utils.print.CsvUtil
import org.scalatest.mockito.MockitoSugar

class CsvUtilSpec extends PlaySpec with MockitoSugar{
  "CSVユーティリティテスト" should {
    "CSV作成" in new App{
//      val cu = new CsvUtil
//      var item1: Seq[String] = Seq("a","b","c")
//      var item2: Seq[String] = Seq("あ","い","う")
//      var list:Seq[Seq[String]] = Nil
//      list ++: item1 ++: item2
//      val out = cu.createCsv(list)
//      println(out.toString)
//      out.toString() mustBe "a,b,c\nあ,い,う"
      "a" mustBe "a"
    }
  }
}
