package utils

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._

object MyConfigUtil {
  private val config :Config = ConfigFactory.load(DefParams.CONFIG_FILE_NAME)

  /**
   * キーに該当する設定情報をリストで取得する。
   * 
   * @param key 設定のキー
   * @return Option[List[String]]　設定値のリスト
   */
  def getByList(key: String) : Option[List[String]] = {
    //java.util.Listで返却されるため、一応JavaConversionsで変換
     Option.apply(config.getStringList(key).toList)
  }
  
    /**
   * キーに該当する設定情報を取得する。
   * 
   * @param key 設定のキー
   * @return String　設定値
   */
  def getByStr(key: String) : Option[String] = {
     Option.apply(config.getString(key))
  }

}