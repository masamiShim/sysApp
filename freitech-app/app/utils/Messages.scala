package utils

trait FeMessages{
  val mes :String
  val mId :String
}

case class DispError(mes:String, mId: String = "") extends FeMessages
case class DispWarn(mes:String, mId: String= "") extends FeMessages
case class DispInfo(mes:String, mId: String= "") extends FeMessages
  
