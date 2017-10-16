package models

import scalikejdbc._
import org.joda.time.DateTime
import utils.MyConfigUtil
import utils.DefParams

case class SessionManager (
   sessionId: String
  , loginId: String
  , expireAt: DateTime
){
  def findByPkey()(implicit session: DBSession = SessionManager.autoSession): Option[SessionManager] = SessionManager.findByPkey(sessionId)(session)
  def create()(implicit session: DBSession = SessionManager.autoSession): Unit = SessionManager.create(sessionId,loginId)(session)
  def destroy()(implicit session: DBSession = SessionManager.autoSession): Unit = SessionManager.destroy(loginId)(session)
  def destroyAllExpired()(implicit session: DBSession = SessionManager.autoSession): Unit = SessionManager.destroyAllExpired()(session)
}

object SessionManager extends SQLSyntaxSupport[SessionManager] {
  
  def apply(s: SyntaxProvider[SessionManager])(rs: WrappedResultSet): SessionManager= apply(s.resultName)(rs)
  def apply(s: ResultName[SessionManager])(rs: WrappedResultSet): SessionManager = new SessionManager(  
   sessionId = rs.get(s.sessionId)
   , loginId = rs.get(s.loginId)
   , expireAt = rs.get(s.expireAt)  
  )
  
  val s = SessionManager.syntax("s")
  private val isNotExpired = sqls.le(s.expireAt, DateTime.now)
  
  /**
   * プライマリーキーからデータを取得する。
   */
  def findByPkey(sessionId: String)(implicit session: DBSession = autoSession): Option[SessionManager] = withSQL {
    select.from(SessionManager as s)
          .where.eq(s.sessionId, sessionId)
          .and.append(isNotExpired)
  }.map(SessionManager(s)).single.apply()

   /**
   * セッションIDの有効期限を確認する。
   */
  def isExpired(sessionId: String)(implicit session: DBSession = autoSession): Boolean = withSQL {
    select.from(SessionManager as s)
          .where.eq(s.sessionId, sessionId)
          .and.append(isNotExpired)
  }.map(SessionManager(s)).single.apply() match {
    case None => true
    case Some(_) => false
  }

  
  /**
   * Insert
   */
  def create(sessionId: String, loginId: String)(implicit session: DBSession = autoSession): Unit = {
    val expireAt: DateTime= calcExpireDateTime
    withSQL {
        insert.into(SessionManager).namedValues(
          column.sessionId -> sessionId
          , column.loginId -> loginId
          , column.expireAt -> expireAt
        )
    }.update().apply()
  }
   
  def destroy(sessionId: String)(implicit session: DBSession = autoSession): Unit = withSQL {
    delete.from(SessionManager).where.eq(column.sessionId, sessionId)
  }.update.apply()

  /**
   * 失効済みのセッションデータを削除する。
   * @param loginId
   */
  def destroyAllExpired()(implicit session: DBSession = autoSession): Unit = withSQL {
    delete.from(SessionManager).where.le(column.expireAt, DateTime.now)
  }.update.apply()

  /**
   * ログインIDに該当する失効済みのセッションデータを削除する。
   * @param loginId*/
  def destroyAllExpiredById(loginId: String)(implicit session: DBSession = autoSession): Unit = withSQL {
    delete.from(SessionManager).where.eq(column.loginId, loginId).and.le(column.expireAt, DateTime.now)
  }.update.apply()

  /**
   * 失効日時を計算する。
   * 現在日時 + 設定ファイルのlogin.session.expireTermの設定値を考慮して取得する。
   * 
   * @throws UnSetSessionLimitException セッションの最大保持期間が取得できない
   */
  private def calcExpireDateTime(): DateTime = {
    //TODO:設定ファイル読み込みから期間を取得するようにリファクタリングできるとよいなー
    val exprDay = MyConfigUtil.getByStr(DefParams.CNF_LOGIN_SESSION_EXPIRE_TERM_DAY).getOrElse("0")
    val exprHour = MyConfigUtil.getByStr(DefParams.CNF_LOGIN_SESSION_EXPIRE_TERM_HOUR).getOrElse("0")
    println(exprDay)
    println(exprHour)
    val result = DateTime.now.plusDays(exprDay.toInt).plusHours(exprHour.toInt) match {
      case t if t.isEqualNow() => {
        //TODO:最大値取得できなかったらExceptionで飛ばしてよいかも
        val exprMaxYears = MyConfigUtil.getByStr(DefParams.CNF_LOGIN_SESSION_EXPIRE_TERM_MAX).getOrElse("0")
        t.plusYears(exprMaxYears.toInt)
      }
      case t => t
    }
    result
  }
}