package utils.print

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import java.io.IOException
import java.io.ByteArrayOutputStream
import java.awt.Color
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Font
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.font.PDType1Font
import java.io.File
import org.apache.pdfbox.contentstream.PDContentStream
import org.apache.fontbox.ttf.TrueTypeCollection
import scala.util.control.Exception.Catch
import org.apache.fontbox.ttf.TrueTypeFont

/**
 * 位置情報
 */
case class PDPos(val x: Float, val y: Float)
/**
 * 線の基本情報
 */
case class LineInfo(val pos: PDPos, val len: Float, val width: Float = 1f, val color: Color = Color.BLACK)

class PdfBoxUtil {
  def createPDF(): ByteArrayOutputStream = {
    val output: ByteArrayOutputStream = new ByteArrayOutputStream
    try {
      val file: File = new File("C:/Windows/Fonts/msmincho.ttc");
      var list: Option[TrueTypeCollection] = None
      try {
        //save前にcloseすると埋め込みできないので注意。
        list = Option(new TrueTypeCollection(file))
        list.map { _list =>
          val builder = new PDDocumentFBuilder(new PDDocument)
          builder.setFont(_list, "MS-Mincho")
          // 空のドキュメントオブジェクトを作成します
          builder.createPage
          builder.strokeLineH(LineInfo(PDPos(2, 10), 100, 1f))
          builder.strokeLineV(LineInfo(PDPos(2, 30), 100, 1f))
          builder.appendText(PDPos(10, 1), "テスト")
          builder.addPage

          // ドキュメントを保存します
          builder.doc.save(output)
          builder.doc.close()
        }

      } catch {
        case e: Exception => {
          println("出力ミス")
        }
      } finally {
        list.map(_l => _l.close)
      }
      output
    } catch {
      case e: IOException => {
        e.printStackTrace
        null
      }

    }
  }

  /**
   * PDFドキュメントを作成するBuilderクラス
   */
  class PDDocumentFBuilder(val doc: PDDocument) {
    val maxline: Int = 200
    var targetPage: PDPage = new PDPage
    var curline: Int = 1
    var contents: Option[PDPageContentStream] = None
    var font: Option[PDFont] = None

    /**
     * ページを作成し操作対象とする。
     */
    def createPage = {
      this.targetPage = new PDPage()
      this.contents = Option(new PDPageContentStream(doc, targetPage))
      this.curline = 0
    }

    /**
     * 現在ページに水平線を引く
     */
    def strokeLineH(li: LineInfo): Unit = {
      //TODO: 引く線が用紙サイズをオーバーしないか確認する。
      strokeLineBase(li.pos, li.pos.x + li.len, li.pos.y, li.width, li.color)
    }

    /**
     * 現在ページに縦線を引く
     */
    def strokeLineV(li: LineInfo): Unit = {
      //TODO: 引く線が用紙サイズをオーバーしないか確認する。
      strokeLineBase(li.pos, li.pos.x, li.pos.y + li.len, li.width, li.color)
    }

    /**
     * 線を引くための基本クラス
     */
    private def strokeLineBase(pos: PDPos, x: Float, y: Float, w: Float, color: Color): Unit = {
      contents.map { _c =>
        _c.moveTo(pos.x, pos.y) // 罫線の始点座標を指定
        _c.lineTo(x, y) // 罫線の終点座標を指定
        _c.setStrokingColor(color) // 罫線の色を指定
        _c.setLineWidth(w) // 罫線の幅を指定
        _c.stroke() // 罫線を引く
      }
    }

    /**
     * 文字を出力するための基本クラス
     */
    def appendText(pos: PDPos, text: String, fontSize: Float = 12f, color: Color = Color.BLACK): Unit = {
      contents.map { _c =>
        _c.beginText()
        _c.setFont(font.getOrElse(PDType1Font.TIMES_ROMAN), fontSize)
        _c.newLineAtOffset(pos.x, pos.y) // 罫線の始点座標を指定
        _c.showText(text) // 罫線の終点座標を指定
        _c.endText()
      }
    }

    /**
     * 改ページとなるか確認
     */
    def willNextPage: Boolean = {
      return curline + 1 > maxline
    }

    /**
     * 指定行を追加すると改ページとなるか確認
     */
    def willNextPageen(len: Int): Boolean = {
      return curline + 1 > maxline
    }

    /**
     * 現在ページを出力ページに追加する
     */
    def addPage = {
      doc.addPage(targetPage)
      contents.map(_c => _c.close)
    }
    def setFont(list: TrueTypeCollection, typeNm: String): Unit = {
      font = Option(PDType0Font.load(doc, list.getFontByName(typeNm), true)) //Font名から取得
    }
  }
}