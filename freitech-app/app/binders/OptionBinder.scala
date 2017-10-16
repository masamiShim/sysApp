package binders

import play.api.mvc.PathBindable
import scala.Left
import scala.Right
import java.awt.geom.PathIterator

/**
 * routesでOptionの引数を使用する場合に必要。
 * play2.1以降はこれを定義+ビルドに含めないと使用できない。
 */
/*
object OptionBinder {

  implicit def optBinder(implicit opt: PathBindable[T]) = new PathBindable[Option[T]] {
    override def bind(key: String, value: String): Either[String, Option[T]] =
      implicitly[PathBindable[T]].
        bind(key, value).
        fold(
          left => Left(left),
          right => Right(Some(right)))

    override def unbind(key: String, value: Option[T]): String = value map (_.toString) getOrElse ""
  }
}
*/

//case class OptArg(v: T)
/*object OptArg {
  
  implicit def optBidner[T: PathBindable] = new PathBindable[Option[T]] {
    override def bind(key: String, value: String): Either[String, Option[T]] = {
      for {
        v <- optBidner.bind(key, value).right
      } yield v
    }
    override def unbind(key: String, value: Option[T]): String = {
      optBidner.unbind(key, value.getOrElse(""))
    }

  }
}
*/
