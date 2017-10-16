package validations

trait ValidatorBase

trait InputValidator extends ValidatorBase {
  lazy val NUM = """\d+"""
  lazy val ALL_NUM = """[\d０-９]+"""
  lazy val DEC = """\d.*[.].\d+"""
  lazy val ALPHA = """[\\u\\l]+"""
  lazy val ALPHA_UPPER = """[A-Z]+"""
  lazy val TEL = """\b\d{1,3}(-\d{3})*\b"""
  lazy val ALL_H_KANA = """[あ-ん]+"""
  lazy val ALL_KANA = """[ア-ヴ]+"""
  lazy val HALF_KANA = """[ｦ-ﾟ]+"""
  lazy val MAIL = """/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/"""

  /** Web禁則文字*/
  lazy val WEB_NG = """[&<>"']+"""

  /** Unicodeマッピングと差が出る文字*/
  lazy val UNICODE_NG = """[¢£¬‖−〜―]+"""

  /** SQLエスケープ →　O/Rマッパー使ってればあるでしょう */
  lazy val SQL_ESCAPE = """['"%_％＿\]+"""

  /**
   * 整数値チェック。
   */
  def isNum(v: Object): Boolean = {
    v.toString().matches(NUM)
  }

  /**
   * 小数値チェック。
   */
  def isDecimalNum(v: Object): Boolean = {
    v.toString().matches(DEC)
  }

  /**
   * 記号チェック。
   */
  def isSymbol: Boolean = {
    true
  }

  /**
   * 半角英字チェック。
   */
  def isAlpha(v: Object): Boolean = {
    v.toString().matches(ALPHA) || v.toString().matches(ALPHA_UPPER)
  }

  /**
   * 半角英数字チェック。
   */
  def isAlphaNum(v: Object): Boolean = {
    v.toString().matches(ALPHA) || v.toString().matches(NUM)
  }

  /**
   * 英大文字チェック。
   */
  def isUpprAlpha(v: Object): Boolean = {
    v.toString().matches(ALPHA_UPPER)
  }

  /**
   * 半角英字チェック。
   */
  def isLwrAlpha(v: Object): Boolean = {
    v.toString().matches(ALPHA)
  }

  /**
   * 電話番号チェック。
   */
  def isTelNo(v: Object): Boolean = {
    v.toString().matches(TEL)
  }

  /**
   * メールアドレスチェック
   */
  def isMailAddr(v: Object): Boolean = {
    v.toString().matches(MAIL)
  }

  /**
   * 全角かな/カナチェック
   */
  def isKana(v: Object): Boolean = {
    v.toString().matches(ALL_H_KANA) || v.toString().matches(ALL_KANA)
  }

  /**
   * 半角カナチェック
   */
  def isHalfKana(v: Object): Boolean = {
    v.toString().matches(HALF_KANA)
  }

  /**
   * 禁則文字チェック
   */
  def hasForbiddenChar: Boolean
}
