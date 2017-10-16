package exceptions

/**
 * 　業務上で起こりうる例外のルートException
 */
//TODO:ログレベルとかをExceptionで分けられるとよいかも
trait BuissinessException extends RuntimeException

/**　ユーザ情報が取得できない場合 */
class UserNotFoundException extends BuissinessException
/**　セッションの有効期限が切れた場合 */
class SessionExpireException extends BuissinessException
