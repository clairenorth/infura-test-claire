package cn.ethereum.model

import scala.util.control.NoStackTrace

trait GeneralError extends Throwable {
  val msg: Option[String]
}

case class InvalidConfig(e: String) extends NoStackTrace with GeneralError {
  override val msg: Option[String] =
    Some(s"Invalid config due to ${e}")
}

case class InvalidBlockParam(e: Throwable)
    extends NoStackTrace
    with GeneralError {
  override val msg: Option[String] =
    Some(
      s"Invalid block parameter. Must be a block number of block string: ${e.getMessage}")
}
