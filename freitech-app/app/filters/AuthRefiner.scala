package filters

import play.api.mvc.Request
import scala.concurrent.Future
import play.api.mvc.ActionRefiner
import play.api.mvc.Result
import play.api.mvc.Results
import play.api.mvc.Results._
import models.Member
import play.api.mvc.ActionFilter
import play.api.mvc.WrappedRequest
import play.api.mvc.EssentialFilter
import play.api.mvc.EssentialAction
import play.api.mvc.RequestHeader
import play.api.libs.iteratee.Done
import javax.inject.Inject
import scala.concurrent.ExecutionContext
import play.api.libs.streams.Accumulator
import akka.util.ByteString
import play.api.Logger
import models.SessionManager

class UserRequest[A](memberId: String,request: Request[A]) extends WrappedRequest[A](request)

case class MenuRequest[A](val member: Member, request: UserRequest[A])extends WrappedRequest[A](request)

object TekitoAction extends ActionRefiner[UserRequest,MenuRequest] {
  def refine[A](input: UserRequest[A]) = Future.successful{
    println("TekitoActionです")
    val mem = Member.findByPkey(input.session.get("loginId").getOrElse(""))
    mem match {
      case Some(m) => Right(MenuRequest(m,input))
      case None => Left(Ok(""))
    }
  }
}

/**
 * ログイン時にセッションIDとログインユーザのIDをマスタに登録しておき、確認するようにする。
 * 一応、sesionId+keyがないとアクセス不可とする。
 */
class AuthFilter @Inject()(implicit ec: ExecutionContext) extends EssentialFilter{
  def apply(next: EssentialAction) = new EssentialAction{
    def apply(req: RequestHeader) = {
      val accumulator:  Accumulator[ByteString,Result] = next(req)
      accumulator.map{result =>
        if(req.headers.get("Auth-Checked").isDefined){         
          req.session.get("sessionId") match {
            case None => {
              result.withHeaders("Auth-Checked" -> "false")
              Redirect(controllers.routes.LoginController.index)
            }
            case Some(key) => {
                  //セッションID有効チェック処理
                  if(SessionManager.isExpired(key)){
                    Redirect(controllers.routes.LoginController.index)
                  }
            }
          }
        }
        result.withHeaders("X-Auth-Checked" -> "true")
      }
    }
  }
}

/**
 * リクエストのロギング用フィルター
 * リクエストの以下の情報を出力する。
 * ・メソッド
 * ・URI
 * ・処理時間
 * ・ステータス 
 */
class LoggingFilter @Inject()(implicit ec: ExecutionContext) extends EssentialFilter{
  def apply(next: EssentialAction) = new EssentialAction{
    def apply(req: RequestHeader) = {
      val startTime =   System.currentTimeMillis();
      val accumulator:  Accumulator[ByteString,Result] = next(req)
      accumulator.map{result => 
        val endTime = System.currentTimeMillis();
        val requestTime = endTime - startTime
        Logger.info(s"${req.method} ${req.uri} took ${requestTime}ms and returned ${result.header.status}")
        result.withHeaders("Request-Time" -> requestTime.toString)
      }
    }
  }
}
