package controllers

import javax.inject.Inject
import play.api.i18n.MessagesApi
import play.api.mvc.Action
import play.api.data.Forms._
import play.api.data.Form
import javax.inject.Singleton
import forms.LoginForm
import models.Member
import utils.FeMessages
import utils.DispError
import utils.SessionUtil
import services.LoginService

@Singleton
class LoginController @Inject() (val messagesApi: MessagesApi) extends ControllerBase with LoginService  {
   val loginForm = Form(
    mapping(
      "loginId" -> nonEmptyText(maxLength = 20)
      , "pass" -> nonEmptyText(maxLength = 20))(LoginForm.apply)(LoginForm.unapply))

  def index() = Action { 
    Ok(views.html.login.login("ログイン画面", Nil, loginForm))
  }
  /**
   * ログイン処理
   */
  def login() = Action {implicit req =>
    loginForm.bindFromRequest().fold(
      errors => {
        print(errors)
        BadRequest(views.html.login.login("ログイン画面", Nil, loginForm.bindFromRequest()))

      },
      success => {
          //ログインIDから会員情報を取得する。
         val member :Option[Member] =  Member.findByPkey(loginForm.bindFromRequest.get.loginId)
         member match { 
             case None => 
               BadRequest(views.html.login.login("ログイン画面", Nil:+DispError("ログインIDに該当するユーザは登録されていません。"), loginForm.bindFromRequest()))
             case Some(_mem :Member) if !_mem.password.equals(loginForm.bindFromRequest.get.pass) => {_mem ->
               Member.loginFailure(_mem)
               BadRequest(views.html.login.login("ログイン画面", Nil:+DispError("パスワードが違います"), loginForm.bindFromRequest()))    
             }
             case Some(_mem :Member) if isDeny(_mem) => 
               BadRequest(views.html.login.login("ログイン画面", Nil:+DispError("ログイン失敗回数を超過しました。時間をおいてログインしてください。"), loginForm.bindFromRequest()))    
             case Some(_mem :Member) => {         //トップへ移動
               val sessionId = SessionUtil.registSession(_mem.loginId);
               Ok(views.html.top("トップ", Nil)).withSession(req.session + ("sessionId", sessionId))
             }
         }
    
      }) 
  }
}