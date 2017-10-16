package filters

import play.api.mvc.ActionBuilder
import play.api.mvc.Request
import scala.concurrent.Future
import play.api.mvc.Results
import play.api.mvc.Result
import play.Logger

case class UserInfo(
    var id: Long
    ,var name: String
    ,var authCls: String
    ,var otherAuth: String
    )

/**
 * 認証付きAction　セッション切れの場合、ログインページへ飛ばす。
 */
object AuthAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block:(Request[A]) => Future[Result]) = {
    //TODO: セッションキーを可変に出来るようにする
    val sessionId: String = request.session("")
    Logger.info("TODO: Logging SessionId")
    //TODO: DBからユーザの情報を取得する
    val  info: Option[UserInfo] = None
    Logger.info("TODO: Result")
    info match {
//      case None => println("")
      case _ => block(request)
    }
  }
}