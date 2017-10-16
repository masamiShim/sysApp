package models

import scalikejdbc._
import org.joda.time.DateTime

case class Member (
   id: Option[Long] = None
  , loginId: String
  , password: String
  , sei: String
  , mei: String
  , tel: String
  , email: String
  , loginFailuredCount: Int
  , createdAt: Option[DateTime]
  , modifiedAt: Option[DateTime] = None
  , lastFailuredAt: Option[DateTime] = None
  , deletedAt: Option[DateTime] = None
){
  def save()(implicit session: DBSession = Member.autoSession): Member = Member.save(this)(session)
  def destroy()(implicit session: DBSession = Member.autoSession): Unit = Member.destroy(loginId)(session)
}

object Member extends SQLSyntaxSupport[Member] {
  
  def apply(m: SyntaxProvider[Member])(rs: WrappedResultSet): Member = apply(m.resultName)(rs)
  def apply(m: ResultName[Member])(rs: WrappedResultSet): Member = new Member(  
   id = rs.get(m.id)
   , loginId = rs.get(m.loginId)
   , password = rs.get(m.password)
   , sei = rs.get(m.sei)
   , mei = rs.get(m.mei)
   , tel = rs.get(m.tel)
   , email = rs.get(m.email)
   , loginFailuredCount = rs.get(m.loginFailuredCount)
   , createdAt = rs.get(m.createdAt)
   , modifiedAt = rs.get(m.modifiedAt)
   , lastFailuredAt = rs.get(m.lastFailuredAt)
   , deletedAt = rs.get(m.deletedAt)  
  )
  
  val m = Member.syntax("m")
  private val isNotDeleted = sqls.isNull(m.deletedAt)
  
  /**
   * プライマリーキーからデータを取得する。
   */
  def findByPkey(loginId: String)(implicit session: DBSession = autoSession): Option[Member] = withSQL {
    select.from(Member as m)
          .where.eq(m.loginId, loginId)
          .and.append(isNotDeleted)
  }.map(Member(m)).single.apply()
  
  /**
   * 削除フラグを除いた全件データを取得する  
   */
  def all()(implicit session: DBSession = autoSession): List[Member] = withSQL{
    select.from(Member as m)
          .where.append(isNotDeleted)
          .orderBy(m.id)
  }.map(Member(m)).list.apply()
  
  /**
   * Insert
   */
  def create(member: Member, createdAt: DateTime = DateTime.now())(implicit session: DBSession = autoSession): Member = {
       val id = withSQL {
        insert.into(Member).namedValues(
          column.loginId -> member.loginId
          , column.password -> member.password
          , column.sei -> member.sei
          , column.mei -> member.mei
          , column.tel -> member.tel
          , column.email -> member.email
          , column.loginFailuredCount -> 0
          , column.createdAt -> createdAt
        )
    }.updateAndReturnGeneratedKey.apply()
    member
  }
  
  /**
   * update
   */
  def save(member: Member, modifiedAt: DateTime = DateTime.now())(implicit session: DBSession = autoSession): Member = {
   withSQL {
     update(Member).set(
         column.id -> member.id
         ,column.loginId -> member.loginId
         , column.sei -> member.sei
         , column.mei -> member.mei
         , column.tel -> member.tel
         , column.email -> member.email
         , column.modifiedAt -> member.modifiedAt
     ).where.eq(column.loginId, member.loginId).and.isNull(column.deletedAt)
   }.update().apply()
   member
  }
  
  def destroy(loginId: String)(implicit session: DBSession = autoSession): Unit = withSQL {
    update(Member).set(column.deletedAt -> DateTime.now).where.eq(column.loginId, loginId)
  }.update.apply()
 
    /**
   * ログイン失敗時に以下を更新する。
   * 1.ログイン失敗回数 + 1
   * 2.最終ログイン失敗日時
   */
  def loginFailure(member: Member)(implicit session: DBSession = autoSession): Member = {
   withSQL {
     update(Member).set(
          column.loginFailuredCount -> (member.loginFailuredCount + 1)
         , column.lastFailuredAt -> DateTime.now
     ).where.eq(column.loginId, member.loginId)
   }.update().apply()
   member
  }
 
 
}