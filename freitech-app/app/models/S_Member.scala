package models

import org.joda.time.DateTime

case class S_Member (
  var id: Option[Long]
  ,var loginId: String
  ,var password: String
  ,var sei: String
  ,var mei: String
  ,var tel: String
  ,var email: String
  ,var created: Option[DateTime]
  ,var modified: Option[DateTime]
)
  
  /**
   *　会員情報
   */
  case class S_PostMemberRequest(
  loginId: String
  , password: String
  , confirmed: String
  , sei: String
  , mei: String
  , tel: String
  , email: String
)

  /**
   *　会員情報検索
   */
  case class S_SearchMemberRequest(
  loginId: String
  , sei: String
  , mei: String
)