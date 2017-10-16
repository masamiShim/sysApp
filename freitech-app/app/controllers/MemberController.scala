package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.i18n.MessagesApi
import play.api.data.Forms._
import play.api.data.Form
import javax.inject.Inject
import play.api.mvc.Action
import scala.collection.immutable.Nil
import org.joda.time.DateTime
import scala.util.Try
import models.Member
import javax.inject.Singleton
import forms.MemberForm
import forms.MemberForm.SearchCondForm
import utils.DispError
import utils.DispWarn
import utils.DispInfo
import utils.print.PdfBoxUtil
import utils.print.PdfBoxUtil
import java.io.ByteArrayOutputStream

@Singleton
class MemberController @Inject() (val messagesApi: MessagesApi) extends ControllerBase  {
 val searchCond = Form(
    mapping(
      "loginId" -> text(maxLength = 20)
      , "sei" -> text(maxLength = 20)
      , "mei" -> text(maxLength = 20))(SearchCondForm.apply)(SearchCondForm.unapply))

  val memberForm = Form(
    mapping(
       "id" -> optional(longNumber) 
      , "loginId" -> text(maxLength = 20)
      , "password" -> text(maxLength = 20)
      , "sei" -> text(maxLength = 30)
      , "mei" -> text(maxLength = 30)
      , "tel" -> text(maxLength = 13)
      , "email" -> text(maxLength = 255)
      , "loginFailuredCount" -> ignored(0)
      , "createdAt" -> optional(jodaDate)
      , "modifiedAt" -> optional(jodaDate)
      , "lastFailuredAt" -> optional(jodaDate)
      , "deltedAt" -> optional(jodaDate)
      )(Member.apply)(Member.unapply))

  def index() = Action { implicit request =>  
    Ok(views.html.member.index("利用者検索画面", Nil, searchCond, Nil))
  }

  def result() = Action { implicit request =>
       val members = Member.all()
          if (!members.isEmpty) {
            Ok(views.html.member.index("利用者検索画面", Nil, searchCond, members))
          }else{
            Ok(views.html.member.index("利用者検索画面", Nil, searchCond, Nil))
          }
  }
     
    /*
    searchCond.bindFromRequest.fold(
      errors => {
        print(errors)
        BadRequest(views.html.member.index("利用者検索画面", "利用者を検索してください。", searchCond, Nil))
      },
      success => {
        memberDao.findBySearchCond(searchCond.bindFromRequest().get).map { res =>
          if (!res.seq.isEmpty) {
            Ok(views.html.member.index("利用者検索画面", "利用者を検索してください。", searchCond, res.seq))
          }
        }
        Ok(views.html.member.index("利用者検索画面", "利用者を検索しました。", searchCond, Nil))
      })
       
      */
  def input() = Action {
    Ok(views.html.member.input("利用者情報登録画面", Nil :+ DispError("test1") :+ DispError("test2") :+ DispError("test3"), memberForm,false))
  }
  
  def confirm() = Action { implicit request =>
    Ok(views.html.member.confirm("利用者情報確認画面", Nil, memberForm.bindFromRequest()))
  }
  
  def create() = Action { implicit request =>
    print(request.rawQueryString)
    memberForm.bindFromRequest.fold(
      errors => {
        print(errors)
        BadRequest(views.html.member.confirm("利用者情報確認画面",Nil, memberForm.bindFromRequest()))

      },
      success => {
        val mem = memberForm.bindFromRequest().get 
        Member.create(mem)
        Ok(views.html.member.complete("利用者情報登録完了画面", Nil))
      })
  }
  
  /**
   * 更新画面へ遷移する。
   */
  def update(loginId: String) = Action {
    //取得→更新画面　取得NG→検索画面
    Member.findByPkey(loginId) match { 
      case Some(m) =>  Ok(views.html.member.input("利用者情報更新画面", Nil, memberForm.fill(m),true))
      case None => Redirect(routes.MemberController.index)
    } 
  }
  
  def delete(id: Option[String]) = Action {
    Ok("delete")
/*    loginId match {
      case None => BadRequest(views.html.member.index("利用者情報検索画面", "時間をおいて処理してみてください。",searchCond, Nil))
      case Some(id) => {
        memberDao.findById(Try(id.toLong).getOrElse(0)).map{f => Ok(views.html.member.confirm("利用者情報削除確認画面", "削除します。よろしいですか？", memberForm))}
      }
    }
  */
  }
  def download() = Action {
      val output :ByteArrayOutputStream = new PdfBoxUtil().createPDF()
        Ok(output.toByteArray())
        .withHeaders("Content-Type" -> "application/force-download"
                      ,"Content-Disposition" -> "attachment; filename=\"yourFile.pdf\"")
  }
  
}