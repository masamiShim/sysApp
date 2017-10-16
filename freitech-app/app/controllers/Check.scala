/*package controllers

import javax.inject.Inject
import play.api.i18n.MessagesApi
import play.api.mvc.Action
import play.api.data.Form
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.data.Forms._
import java.util.Date
import dao.ChecksDao
import models.ResultPostRequest
import java.sql.Timestamp
import org.joda.time.DateTime
import play.api.mvc.RequestHeader

class Check @Inject()(val messagesApi: MessagesApi)(val checkDao: ChecksDao) extends ControllerBase {
  //リクエストフォームの設定  
  val f = Form(
    mapping(
      "name" -> nonEmptyText)(ResultPostRequest.apply)(ResultPostRequest.unapply))

  def index() = Action {implicit request  =>
    Ok(views.html.check.index("play診断", "あなたにお勧めなPlay frameworkのバージョンは?", f))
  }

  def result() = Action {implicit request  => 
    f.bindFromRequest.fold(
      errors => {
        print(errors)
        BadRequest(views.html.check.index("play診断", "あなたにお勧めなPlay frameworkのバージョンは?", f))
      },
      success => {
        val rf: ResultPostRequest = f.bindFromRequest().get

        val res: models.Check = new models.Check(None, rf.name, "おすすめは…", new DateTime(), new DateTime())
        print(rf.name)
        Ok(views.html.check.result("結果", res))
      })
  }

  def resultId(id: Option[Long]) = Action {implicit request  =>
    checkDao.findById(id.getOrElse(0L)).map { res =>
      if(res.isDefined) {
        Ok(views.html.check.result(res.get.name, res.get))
      }
    }
    Ok(views.html.check.index("error", "診断エラーです",f))
  }
  
  def recent(page: Int) = Action{implicit request  =>
    checkDao.findByWithPages(page, 20).map{ res =>
      if(!res.isEmpty) Ok(views.html.check.recent("","",res,page,checkDao.maxPage(20)))
    }
    Ok(views.html.check.recentEmpty("診断結果なし","もう一度やり直してください。	"))
    
  }

}
*/