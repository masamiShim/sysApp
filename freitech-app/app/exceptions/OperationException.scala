package exceptions

/**
 * 　運用上のミスで起こりうる例外のルートException
 */
trait OperationException extends RuntimeException

/**　設定内容が取得できない場合 */
class ConfigNotFoundException extends OperationException

/**　セッション有効期限の上限値が設定されていない場合 */
class UnSetSessionLimitException extends OperationException
