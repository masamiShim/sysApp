package forms
import play.api.data.Forms._
import play.api.data.Form
import models.Member

object MemberForm {
   //リクエストフォームの設定  
  case class SearchCondForm(loginId:String,sei: String, mei:String)
 
  //リクエストフォームの設定  
  case class MemberForm(loginId:String,password: String, sei: String, mei:String,tel: String, email: String)
 
}