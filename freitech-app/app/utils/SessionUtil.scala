package utils

import models.SessionManager
import models.Member

object SessionUtil {

  /**
   * セッション管理されているIDと重複しないUUIDを取得する。
   * @return uuid(セッション管理されていない、または失効しているID)
   */
  private def generateUUID(): String = {
    val uuid = java.util.UUID.randomUUID().toString()
    SessionManager.findByPkey(uuid) match {
      case None     => uuid
      case Some(id) => generateUUID()
    }
  }
  
  /**
   * セッションテーブルに登録する。
   * 併せて、無効なセッション情報は削除する。
   * @return セッションID
   */
  def registSession(loginId: String): String = {
    val sessionId = this.generateUUID()
    SessionManager.destroy(sessionId)
    SessionManager.create(sessionId, loginId)
    SessionManager.destroyAllExpiredById(loginId)
    sessionId
  }
}