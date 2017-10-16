package services

import models.Member

trait LoginService {
  
  /**
   * 入力されたID/PASSの組み合わせが正しいか確認する
   *  @return true 正しいID/PASS
   *
  */
  def isCorrectIdPass(): Boolean = {
    true
  }

  /**
   * ログイン禁止期間か確認する
   *  @return true ログイン禁止期間中
   */
  def isDeny(m: Member): Boolean = {
    //TODO: ログイン禁止回数を超えている。
    //TODO: ログイン禁止期間内
    //TODO: ログイン禁止期間内
    false
  }

}