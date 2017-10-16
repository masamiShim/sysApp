package utils.print

import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import com.opencsv.CSVWriter

class CsvUtil {
  val DEF_ENCODE = "utf-8"

  def createCsv(list: Seq[Seq[String]]): ByteArrayOutputStream = {
    val output: ByteArrayOutputStream = new ByteArrayOutputStream
    var writer: Option[CSVWriter] = None
    try {
      writer = Option(new CSVWriter(new OutputStreamWriter(output, DEF_ENCODE)))
      writer.map { _w =>
        for (l <- list) yield _w.writeNext(l.toArray)
      }
    } catch {
      case e: Exception => {
        println("出力ミス")
      }
    } finally {
      writer.map { _w =>
        _w.flush()
        _w.close()
      }

    }

    output

  }
}