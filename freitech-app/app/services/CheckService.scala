/*
package services

import dao.ChecksDao
import javax.inject.Inject
import utils.ConfigUtil
import models.Check

/**
 * Daoに突っ込む前にごにょごにょしたい場合はここに
 */
class CheckService @Inject() (val checkDao: ChecksDao) {
  def all(id: Long): Seq[Check] = {
    checkDao.all().value.get.getOrElse(Nil)
  }
  def getResult(name: String): Option[List[String]] = {
    ConfigUtil.getByList("checkyou.setting.answer")
  }
}
*/