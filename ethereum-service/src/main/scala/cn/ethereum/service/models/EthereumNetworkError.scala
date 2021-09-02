package cn.ethereum.service.models

import cn.ethereum.model.GeneralError
import org.http4s.Status

import scala.util.control.NoStackTrace

case class EthereumNetworkError(e: Throwable)
    extends NoStackTrace
    with GeneralError {
  override val msg: Option[String] = Some(e.getMessage)
}
